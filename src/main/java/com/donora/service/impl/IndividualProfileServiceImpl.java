package com.donora.service.impl;

import com.donora.dto.IndividualProfileRequest;
import com.donora.dto.IndividualProfileResponse;
import com.donora.entity.IndividualProfile;
import com.donora.entity.User;
import com.donora.repository.IndividualProfileRepository;
import com.donora.repository.UserRepository;
import com.donora.service.IndividualProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IndividualProfileServiceImpl implements IndividualProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IndividualProfileRepository profileRepository;

    @Override
    public IndividualProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        IndividualProfile profile = profileRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return mapToResponse(user, profile);
    }

    @Override
    public IndividualProfileResponse updateMyProfile(String email, IndividualProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        IndividualProfile profile = profileRepository.findByUser(user)
                .orElse(new IndividualProfile());

        profile.setUser(user);
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setPinCode(request.getPinCode());
        profile.setOccupation(request.getOccupation());
        profile.setGender(request.getGender());

        IndividualProfile saved = profileRepository.save(profile);

        return mapToResponse(user, saved);
    }

    private IndividualProfileResponse mapToResponse(User user, IndividualProfile profile) {
        IndividualProfileResponse response = new IndividualProfileResponse();
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setAddress(profile.getAddress());
        response.setCity(profile.getCity());
        response.setState(profile.getState());
        response.setPinCode(profile.getPinCode());
        response.setOccupation(profile.getOccupation());
        response.setGender(profile.getGender());
        return response;
    }
}
