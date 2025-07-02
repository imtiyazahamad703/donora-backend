package com.donora.service;

import com.donora.dto.IndividualProfileRequest;
import com.donora.dto.IndividualProfileResponse;

public interface IndividualProfileService {
    IndividualProfileResponse getMyProfile(String email);
    IndividualProfileResponse updateMyProfile(String email, IndividualProfileRequest request);
}
