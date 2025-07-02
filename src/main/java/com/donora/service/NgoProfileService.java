package com.donora.service;

import com.donora.dto.NgoImpactStatsResponse;
import com.donora.dto.NgoProfileRequest;
import com.donora.dto.NgoProfileResponse;
import com.donora.dto.NgoPublicResponse;

import java.util.List;

public interface NgoProfileService {

    NgoProfileResponse saveOrUpdateProfile(NgoProfileRequest request, String userEmail);

    NgoProfileResponse getNgoProfile(String userEmail);
    NgoImpactStatsResponse getImpactStats(String ngoEmail);
    List<NgoPublicResponse> getAllPublicNgos();

}
