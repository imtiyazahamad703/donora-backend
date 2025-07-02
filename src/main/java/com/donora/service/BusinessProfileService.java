package com.donora.service;

import com.donora.dto.BusinessProfileRequest;
import com.donora.dto.BusinessProfileResponse;

public interface BusinessProfileService {
    BusinessProfileResponse saveOrUpdateProfile(BusinessProfileRequest request, String businessEmail);
    BusinessProfileResponse getBusinessProfile(String businessEmail);
}
