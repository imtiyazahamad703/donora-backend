package com.donora.controller;

import com.donora.dto.ItemRequestPublicResponse;
import com.donora.dto.ItemRequestResponse;
import com.donora.dto.NgoPublicResponse;
import com.donora.service.ItemRequestService;
import com.donora.service.NgoProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicItemRequestController {

    @Autowired
    private ItemRequestService itemRequestService;

    @Autowired
    private NgoProfileService ngoProfileService;

    @GetMapping("/emergency-needs")
    public ResponseEntity<List<ItemRequestResponse>> getAllEmergencyNeeds() {
        List<ItemRequestResponse> emergencies = itemRequestService.getAllEmergencyNeeds();
        return ResponseEntity.ok(emergencies);
    }

    @GetMapping("/needs")
    public ResponseEntity<List<ItemRequestPublicResponse>> getAllOpenNeeds() {
        List<ItemRequestPublicResponse> needs = itemRequestService.getAllOpenRequests();
        return ResponseEntity.ok(needs);
    }

    @GetMapping("/ngos")
    public ResponseEntity<List<NgoPublicResponse>> getAllNgos() {
        List<NgoPublicResponse> ngos = ngoProfileService.getAllPublicNgos();
        return ResponseEntity.ok(ngos);
    }
}
