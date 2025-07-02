package com.donora.service.impl;

import com.donora.dto.ItemRequestPublicResponse;
import com.donora.dto.ItemRequestRequest;
import com.donora.dto.ItemRequestResponse;
import com.donora.dto.kafka.EmergencyRequestKafkaMessage;
import com.donora.entity.ItemRequest;
import com.donora.entity.NgoProfile;
import com.donora.entity.User;
import com.donora.enums.DonationStatus;
import com.donora.enums.RequestStatus;
import com.donora.enums.UrgencyLevel;
import com.donora.kafka.producer.EmergencyRequestKafkaProducer;
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

    @Autowired
    private EmergencyRequestKafkaProducer emergencyRequestKafkaProducer;


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

        // ✅ Step 1: Mark as emergency in DB
        request.setEmergency(true);
        itemRequestRepository.save(request);

        // ✅ Step 2: Print debug log
        System.out.println("🔥 Inside markItemRequestAsEmergency: Will now send Kafka message");

        // ✅ Step 3: Build Kafka DTO
        EmergencyRequestKafkaMessage message = new EmergencyRequestKafkaMessage();
        message.setNgoEmail(ngo.getEmail());
        message.setItemName(request.getItemName());
        message.setQuantity(request.getQuantity());
        message.setUrgencyLevel(request.getUrgencyLevel().name());
        message.setDescription(request.getDescription());

        // ✅ Step 4: Send Kafka message
        emergencyRequestKafkaProducer.sendEmergencyRequest(message);
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

    // ItemRequestServiceImpl.java
    @Override
    public List<ItemRequestPublicResponse> getAllOpenRequests() {
        List<ItemRequest> openRequests = itemRequestRepository.findByStatus(RequestStatus.OPEN);

        return openRequests.stream().map(request -> {
            ItemRequestPublicResponse response = new ItemRequestPublicResponse();
            response.setId(request.getId());
            response.setItemName(request.getItemName());
            response.setDescription(request.getDescription());
            response.setQuantity(request.getQuantity());
            response.setUrgencyLevel(request.getUrgencyLevel());
            response.setCreatedAt(request.getCreatedAt());

            NgoProfile ngoProfile = request.getNgo().getNgoProfile();
            if (ngoProfile != null) {
                response.setNgoName(ngoProfile.getOrganizationName());
                response.setNgoCity(ngoProfile.getCity());
                response.setNgoFocusArea(ngoProfile.getFocusArea());
            } else {
                response.setNgoName(request.getNgo().getName());
            }

            return response;
        }).collect(Collectors.toList());
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
