package com.donora.service.impl;

import com.donora.dto.ItemRequestRequest;
import com.donora.dto.ItemRequestResponse;
import com.donora.entity.ItemRequest;
import com.donora.entity.User;
import com.donora.enums.RequestStatus;
import com.donora.enums.UrgencyLevel;
import com.donora.repository.ItemRequestRepository;
import com.donora.repository.UserRepository;
import com.donora.service.ItemRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ItemRequestResponse createItemRequest(ItemRequestRequest request, String ngoEmail) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO user not found"));

        ItemRequest item = new ItemRequest();
        item.setItemName(request.getItemName());
        item.setQuantity(request.getQuantity());
        item.setUrgencyLevel(UrgencyLevel.valueOf(request.getUrgencyLevel().toUpperCase()));
        item.setDescription(request.getDescription());
        item.setStatus(RequestStatus.OPEN);
        item.setCreatedAt(LocalDateTime.now());
        item.setNgo(ngo);

        ItemRequest saved = itemRequestRepository.save(item);
        return mapToResponse(saved);
    }

    @Override
    public List<ItemRequestResponse> getItemRequestsByNgo(String ngoEmail) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO user not found"));

        List<ItemRequest> requests = itemRequestRepository.findByNgo(ngo);
        return requests.stream().map(this::mapToResponse).collect(Collectors.toList());
    }
    @Override
    public void markItemRequestAsEmergency(Long requestId, String ngoEmail) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        ItemRequest request = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Item request not found"));

        if (!request.getNgo().getId().equals(ngo.getId())) {
            throw new RuntimeException("Access denied: This request does not belong to your NGO.");
        }

        request.setEmergency(true);
        itemRequestRepository.save(request);
    }
    /* shorthand
    @Override
    public List<ItemRequestResponse> getAllEmergencyNeeds() {
        List<ItemRequest> emergencies = itemRequestRepository.findByIsEmergencyTrue();
        return emergencies.stream().map(this::mapToResponse).toList();
    } */

    @Override
    public List<ItemRequestResponse> getAllEmergencyNeeds() {
        List<ItemRequest> emergencies = itemRequestRepository.findByIsEmergencyTrue();
        return emergencies.stream()
                .map(item -> this.mapToResponse(item))
                .toList();
    }



    private ItemRequestResponse mapToResponse(ItemRequest item) {
        ItemRequestResponse dto = new ItemRequestResponse();
        dto.setId(item.getId());
        dto.setItemName(item.getItemName());
        dto.setQuantity(item.getQuantity());
        dto.setUrgencyLevel(item.getUrgencyLevel().name());
        dto.setDescription(item.getDescription());
        dto.setStatus(item.getStatus().name());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setEmergency(item.isEmergency());

        return dto;
    }
}
