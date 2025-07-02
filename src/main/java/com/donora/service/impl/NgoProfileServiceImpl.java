package com.donora.service.impl;

import com.donora.dto.NgoImpactStatsResponse;
import com.donora.dto.NgoProfileRequest;
import com.donora.dto.NgoProfileResponse;
import com.donora.dto.NgoPublicResponse;
import com.donora.dto.kafka.EmergencyRequestKafkaMessage;
import com.donora.entity.*;
import com.donora.enums.DonationStatus;
import com.donora.kafka.producer.EmergencyRequestKafkaProducer;
import com.donora.repository.*;
import com.donora.service.NgoProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgoProfileServiceImpl implements NgoProfileService {

    @Autowired
    private NgoProfileRepository ngoProfileRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemDonationRepository itemDonationRepository;
    @Autowired
    private FoodDonationRepository foodDonationRepository;

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private EmergencyRequestKafkaProducer emergencyRequestKafkaProducer;

    @Override
    public NgoProfileResponse saveOrUpdateProfile(NgoProfileRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NgoProfile profile = ngoProfileRepository.findByUser(user)
                .orElse(new NgoProfile());

        profile.setUser(user);
        profile.setOrganizationName(request.getOrganizationName());
        profile.setRegistrationNumber(request.getRegistrationNumber());
        profile.setContactPerson(request.getContactPerson());
        profile.setContactPhone(request.getContactPhone());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPinCode(request.getPinCode());
        profile.setFocusArea(request.getFocusArea());
        profile.setWebsite(request.getWebsite());

        NgoProfile saved = ngoProfileRepository.save(profile);

        return mapToResponse(saved);
    }

    @Override
    public NgoProfileResponse getNgoProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NgoProfile profile = ngoProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(profile);
    }

    @Override
    public NgoImpactStatsResponse getImpactStats(String ngoEmail) {
        User ngo = userRepository.findByEmail(ngoEmail)
                .orElseThrow(() -> new RuntimeException("NGO not found"));

        // ✅ Count totals
        int totalItemAccepted = itemDonationRepository.countByNgoAndStatus(ngo, DonationStatus.ACCEPTED);
        int totalFoodAccepted = foodDonationRepository.countByNgoAndStatus(ngo, DonationStatus.ACCEPTED);

        // ✅ Estimate people/items helped (optional logic)
        List<ItemDonation> acceptedItems = itemDonationRepository.findByNgoAndStatus(ngo, DonationStatus.ACCEPTED);
        List<FoodDonation> acceptedFoods = foodDonationRepository.findByNgoAndStatus(ngo, DonationStatus.ACCEPTED);

        int totalItemQuantity = acceptedItems.stream()
                .mapToInt(ItemDonation::getQuantity)
                .sum();

        int totalFoodQuantity = acceptedFoods.stream()
                .mapToInt(FoodDonation::getQuantity)
                .sum();

        // ✅ Get recent accepted donation names
        List<String> recentItemNames = itemDonationRepository
                .findTop5ByNgoAndStatusOrderByCreatedAtDesc(ngo, DonationStatus.ACCEPTED)
                .stream()
                .map(ItemDonation::getItemName)
                .toList();

        List<String> recentFoodNames = foodDonationRepository
                .findTop5ByNgoAndStatusOrderByCreatedAtDesc(ngo, DonationStatus.ACCEPTED)
                .stream()
                .map(FoodDonation::getFoodName)
                .toList();

        // ✅ Assemble DTO
        NgoImpactStatsResponse response = new NgoImpactStatsResponse();
        response.setTotalAcceptedItemDonations(totalItemAccepted);
        response.setTotalAcceptedFoodDonations(totalFoodAccepted);
        response.setTotalItemsHelped(totalItemQuantity);
        response.setTotalFoodServingsHelped(totalFoodQuantity);
        response.setRecentAcceptedItems(recentItemNames);
        response.setRecentAcceptedFoods(recentFoodNames);

        return response;
    }

    @Override
    public List<NgoPublicResponse> getAllPublicNgos() {
        List<NgoProfile> ngoProfiles = ngoProfileRepository.findAll();
        return ngoProfiles.stream().map(profile -> {
            NgoPublicResponse dto = new NgoPublicResponse();
            dto.setOrganizationName(profile.getOrganizationName());
            dto.setFocusArea(profile.getFocusArea());
            dto.setAddress(profile.getAddress());
            dto.setCity(profile.getCity());
            dto.setState(profile.getState());
            dto.setContactPerson(profile.getContactPerson());
            dto.setContactPhone(profile.getContactPhone());
            dto.setWebsite(profile.getWebsite());
            return dto;
        }).collect(Collectors.toList());
    }



    private NgoProfileResponse mapToResponse(NgoProfile profile) {
        NgoProfileResponse response = new NgoProfileResponse();
        response.setId(profile.getId());
        response.setOrganizationName(profile.getOrganizationName());
        response.setRegistrationNumber(profile.getRegistrationNumber());
        response.setContactPerson(profile.getContactPerson());
        response.setContactPhone(profile.getContactPhone());
        response.setAddress(profile.getAddress());
        response.setCity(profile.getCity());
        response.setState(profile.getState());
        response.setPinCode(profile.getPinCode());
        response.setFocusArea(profile.getFocusArea());
        response.setWebsite(profile.getWebsite());
        return response;
    }
}
