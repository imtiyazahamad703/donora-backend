package com.donora.service.impl;

import com.donora.dto.ItemDonationRequest;
import com.donora.dto.ItemDonationResponse;
import com.donora.dto.kafka.ItemDonationKafkaMessage;
import com.donora.dto.kafka.ItemDonationStatusKafkaMessage;
import com.donora.entity.ItemDonation;
import com.donora.entity.User;
import com.donora.enums.DonationStatus;
import com.donora.kafka.producer.ItemDonationKafkaProducer;
import com.donora.kafka.producer.ItemDonationStatusKafkaProducer;
import com.donora.repository.ItemDonationRepository;
import com.donora.repository.UserRepository;
import com.donora.service.ItemDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemDonationServiceImpl implements ItemDonationService {

    @Autowired
    private ItemDonationRepository itemDonationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemDonationKafkaProducer itemDonationKafkaProducer;
    @Autowired
    private ItemDonationStatusKafkaProducer statusKafkaProducer;


    @Override
    public List<ItemDonationResponse> getItemDonationsForNgo(String ngoEmail) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO user not found"));

        List<ItemDonation> donations = itemDonationRepository.findByNgo(ngo);
        return donations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Override
    public void updateItemDonationStatus(Long donationId, String ngoEmail, String newStatus) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO user not found"));

        ItemDonation donation = itemDonationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Item donation not found"));

        if (!donation.getNgo().getId().equals(ngo.getId())) {
            throw new RuntimeException("Access denied: This donation is not assigned to your NGO.");
        }

        DonationStatus statusEnum;
        try {
            statusEnum = DonationStatus.valueOf(newStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value. Allowed: ACCEPTED or REJECTED");
        }

        donation.setStatus(statusEnum);
        itemDonationRepository.save(donation);

        // ✅ Send Kafka message to notify donor
        ItemDonationStatusKafkaMessage message = new ItemDonationStatusKafkaMessage();
        message.setDonorEmail(donation.getDonor().getEmail());
        message.setItemName(donation.getItemName());
        message.setQuantity(donation.getQuantity());
        message.setNewStatus(donation.getStatus().name());

        statusKafkaProducer.sendItemDonationStatusUpdate(message);
    }

    @Override
    public void createItemDonation(String userEmail, ItemDonationRequest request) {
        User donor = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!donor.getRole().name().equals("USER")) {
            throw new RuntimeException("Only individual users can donate items.");
        }

        User ngo = userRepository.findById(request.getNgoId())
                .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + request.getNgoId()));

        if (!ngo.getRole().name().equals("NGO")) {
            throw new RuntimeException("Selected user is not an NGO.");
        }

        ItemDonation donation = new ItemDonation();
        donation.setItemName(request.getItemName());
        donation.setDescription(request.getDescription());
        donation.setQuantity(request.getQuantity());
        donation.setCondition(request.getCondition());
        donation.setDonor(donor);
        donation.setNgo(ngo);
        donation.setStatus(DonationStatus.PENDING);
        donation.setCreatedAt(LocalDateTime.now());

        ItemDonation savedDonation = itemDonationRepository.save(donation);

// 🔥 Send Kafka Event
        ItemDonationKafkaMessage message = new ItemDonationKafkaMessage();
        message.setDonationId(savedDonation.getId());
        message.setItemName(savedDonation.getItemName());
        message.setQuantity(savedDonation.getQuantity());
        message.setNgoEmail(ngo.getEmail());
        message.setDonorEmail(donor.getEmail());

        itemDonationKafkaProducer.sendItemDonationCreatedEvent(message);

    }



    private ItemDonationResponse mapToResponse(ItemDonation donation) {
        ItemDonationResponse dto = new ItemDonationResponse();

        dto.setId(donation.getId());
        dto.setItemName(donation.getItemName());
        dto.setQuantity(donation.getQuantity());
        dto.setCondition(donation.getCondition().name());
        dto.setDescription(donation.getDescription());
        dto.setStatus(donation.getStatus().name());
        dto.setCreatedAt(donation.getCreatedAt());

        User donor = donation.getDonor();
        if (donor != null) {
            dto.setDonorName(donor.getName());
            dto.setDonorEmail(donor.getEmail());
            dto.setDonorPhone(donor.getPhone());
        }

        return dto;
    }
}
