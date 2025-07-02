package com.donora.service.impl;

import com.donora.dto.BusinessProfileRequest;
import com.donora.dto.BusinessProfileResponse;
import com.donora.entity.BusinessProfile;
import com.donora.entity.User;
import com.donora.repository.BusinessProfileRepository;
import com.donora.repository.UserRepository;
import com.donora.service.BusinessProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusinessProfileServiceImpl implements BusinessProfileService {

    @Autowired
    private BusinessProfileRepository businessProfileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public BusinessProfileResponse saveOrUpdateProfile(BusinessProfileRequest request, String businessEmail) {
        User user = userRepository.findByEmail(businessEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BusinessProfile profile = businessProfileRepository.findByUser(user)
                .orElse(new BusinessProfile());

        profile.setUser(user);
        profile.setOrganizationName(request.getOrganizationName());
        profile.setContactPerson(request.getContactPerson());
        profile.setContactPhone(request.getContactPhone());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPinCode(request.getPinCode());
        profile.setWebsite(request.getWebsite());
        profile.setBusinessCategory(request.getBusinessCategory());

        BusinessProfile saved = businessProfileRepository.save(profile);
        return mapToResponse(saved);
    }

    @Override
    public BusinessProfileResponse getBusinessProfile(String businessEmail) {
        User user = userRepository.findByEmail(businessEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        BusinessProfile profile = businessProfileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Business profile not found"));

        return mapToResponse(profile);
    }

    private BusinessProfileResponse mapToResponse(BusinessProfile profile) {
        BusinessProfileResponse dto = new BusinessProfileResponse();
        dto.setId(profile.getId());
        dto.setOrganizationName(profile.getOrganizationName());
        dto.setContactPerson(profile.getContactPerson());
        dto.setContactPhone(profile.getContactPhone());
        dto.setAddress(profile.getAddress());
        dto.setCity(profile.getCity());
        dto.setState(profile.getState());
        dto.setPinCode(profile.getPinCode());
        dto.setWebsite(profile.getWebsite());
        dto.setBusinessCategory(profile.getBusinessCategory());
        return dto;
    }
}
