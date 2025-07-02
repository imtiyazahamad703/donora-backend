package com.donora.service;

import com.donora.dto.ItemDonationRequest;
import com.donora.dto.ItemDonationResponse;
import com.donora.dto.UserImpactResponse;

import java.util.List;

public interface ItemDonationService {

    List<ItemDonationResponse> getItemDonationsForNgo(String ngoEmail);

    void updateItemDonationStatus(Long donationId, String ngoEmail, String newStatus);
    void createItemDonation(String userEmail, ItemDonationRequest request);
    List<ItemDonationResponse> getDonationsByDonor(String donorEmail);
    List<ItemDonationResponse> getItemDonationsForUser(String userEmail);
    UserImpactResponse getUserImpact(String userEmail);

}
