package com.donora.service.impl;

import com.donora.dto.BusinessImpactResponse;
import com.donora.dto.FoodDonationRequest;
import com.donora.dto.FoodDonationResponse;
import com.donora.dto.kafka.FoodDonationKafkaMessage;
import com.donora.dto.kafka.FoodDonationStatusKafkaMessage;
import com.donora.entity.FoodDonation;
import com.donora.entity.User;
import com.donora.enums.DonationStatus;
import com.donora.kafka.producer.FoodDonationKafkaProducer;
import com.donora.kafka.producer.FoodDonationStatusKafkaProducer;
import com.donora.repository.FoodDonationRepository;
import com.donora.repository.UserRepository;
import com.donora.service.FoodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FoodDonationServiceImpl implements FoodDonationService {

    @Autowired
    private FoodDonationRepository foodDonationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FoodDonationKafkaProducer foodDonationKafkaProducer;

    @Autowired
    private FoodDonationStatusKafkaProducer foodStatusKafkaProducer;



    @Override
    public List<FoodDonationResponse> getFoodDonationsForNgo(String ngoEmail) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO user not found"));

        List<FoodDonation> donations = foodDonationRepository.findByNgo(ngo);
        return donations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<FoodDonationResponse> getFoodDonationsForBusiness(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<FoodDonation> donations = foodDonationRepository.findByDonor(user);

        return donations.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateFoodDonationStatus(Long donationId, String ngoEmail, String newStatus) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO user not found"));

        FoodDonation donation = foodDonationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("Food donation not found"));

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
        foodDonationRepository.save(donation);
        FoodDonationStatusKafkaMessage message = new FoodDonationStatusKafkaMessage();
        message.setDonorEmail(donation.getDonor().getEmail());
        message.setFoodName(donation.getFoodName());
        message.setQuantity(donation.getQuantity());
        message.setNewStatus(donation.getStatus().name());

        foodStatusKafkaProducer.sendFoodDonationStatusUpdate(message);

    }

    @Override
    public void createFoodDonation(String businessEmail, FoodDonationRequest request) {
        User business = userRepository.findByEmail(businessEmail)
                .orElseThrow(() -> new RuntimeException("Business user not found"));

        if (!business.getRole().name().equals("BUSINESS")) {
            throw new RuntimeException("Only business users can donate food.");
        }

        User ngo = userRepository.findById(request.getNgoId())
                .orElseThrow(() -> new RuntimeException("NGO not found with ID: " + request.getNgoId()));

        if (!ngo.getRole().name().equals("NGO")) {
            throw new RuntimeException("Selected user is not an NGO.");
        }

        // ✅ Create and Save to DB
        FoodDonation donation = new FoodDonation();
        donation.setFoodName(request.getFoodName());
        donation.setQuantity(request.getQuantity());
        donation.setExpiryDate(request.getExpiryDate());
        donation.setDescription(request.getDescription());
        donation.setDonor(business); // Donor is business
        donation.setNgo(ngo);
        donation.setStatus(DonationStatus.PENDING);
        donation.setCreatedAt(LocalDateTime.now());

        // ✅ THIS LINE IS MISSING IN YOUR CODE
        foodDonationRepository.save(donation); // 👈 You missed this earlier!

        // ✅ Kafka event
        FoodDonationKafkaMessage kafkaMsg = new FoodDonationKafkaMessage();
        kafkaMsg.setDonorEmail(business.getEmail());
        kafkaMsg.setFoodName(request.getFoodName());
        kafkaMsg.setQuantity(request.getQuantity());
        kafkaMsg.setNgoEmail(ngo.getEmail());

        foodDonationKafkaProducer.sendFoodDonationCreatedEvent(kafkaMsg);
    }

    @Override
    public BusinessImpactResponse getBusinessImpact(String businessEmail) {
        User user = userRepository.findByEmail(businessEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<FoodDonation> donations = foodDonationRepository.findByDonor(user);

        int totalDonations = 0;
        int totalQuantity = 0;
        Set<Long> ngoIds = new HashSet<>();
        List<String> recentFoods = new ArrayList<>();

        for (FoodDonation donation : donations) {
            if (donation.getStatus() == DonationStatus.ACCEPTED) {
                totalDonations++;
                totalQuantity += donation.getQuantity();
                if (donation.getNgo() != null) {
                    ngoIds.add(donation.getNgo().getId());
                }
                recentFoods.add(donation.getFoodName());
            }
        }

        // Limit to last 5 accepted food names
        Collections.reverse(recentFoods);
        recentFoods = recentFoods.stream().limit(5).collect(Collectors.toList());

        BusinessImpactResponse response = new BusinessImpactResponse();
        response.setTotalFoodDonations(totalDonations);
        response.setTotalQuantityDonated(totalQuantity);
        response.setUniqueNgosHelped(ngoIds.size());
        response.setRecentAcceptedFoods(recentFoods);

        return response;
    }




    private FoodDonationResponse mapToResponse(FoodDonation donation) {
        FoodDonationResponse dto = new FoodDonationResponse();

        dto.setId(donation.getId());
        dto.setFoodName(donation.getFoodName());
        dto.setQuantity(donation.getQuantity());
        dto.setAddress(donation.getAddress());
        dto.setStatus(donation.getStatus().name());
        dto.setPickupTime(donation.getPickupTime());
        dto.setExpiryTime(donation.getExpiryTime());
        dto.setCreatedAt(donation.getCreatedAt());
        dto.setNgoName(
                donation.getNgo() != null ? donation.getNgo().getName() : null
        );
        User business = donation.getBusiness();
        if (business != null) {
            dto.setDonorName(business.getName());
            dto.setDonorEmail(business.getEmail());
            dto.setDonorPhone(business.getPhone());
        }

        return dto;
    }
}
