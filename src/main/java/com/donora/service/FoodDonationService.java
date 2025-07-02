package com.donora.service;

import com.donora.dto.BusinessImpactResponse;
import com.donora.dto.FoodDonationRequest;
import com.donora.dto.FoodDonationResponse;
import java.util.List;

public interface FoodDonationService {

    List<FoodDonationResponse> getFoodDonationsForNgo(String ngoEmail);

    void updateFoodDonationStatus(Long donationId, String ngoEmail, String newStatus);
    void createFoodDonation(String businessEmail, FoodDonationRequest request);
    List<FoodDonationResponse> getFoodDonationsForBusiness(String email);
    BusinessImpactResponse getBusinessImpact(String businessEmail);

}
