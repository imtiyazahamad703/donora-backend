package com.donora.service;

import com.donora.dto.ItemRequestPublicResponse;
import com.donora.dto.ItemRequestRequest;
import com.donora.dto.ItemRequestResponse;

import java.util.List;

public interface ItemRequestService {

    ItemRequestResponse createItemRequest(ItemRequestRequest request, String ngoEmail);

    List<ItemRequestResponse> getItemRequestsByNgo(String ngoEmail);
    void markItemRequestAsEmergency(Long requestId, String ngoEmail);
    List<ItemRequestResponse> getAllEmergencyNeeds();
    List<ItemRequestPublicResponse> getAllOpenRequests();
}
