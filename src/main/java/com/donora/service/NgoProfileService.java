package com.donora.service;

import com.donora.dto.NgoImpactStatsResponse;
import com.donora.dto.NgoProfileRequest;
import com.donora.dto.NgoProfileResponse;

public interface NgoProfileService {

    NgoProfileResponse saveOrUpdateProfile(NgoProfileRequest request, String userEmail);

    NgoProfileResponse getNgoProfile(String userEmail);
    NgoImpactStatsResponse getImpactStats(String ngoEmail);



}
