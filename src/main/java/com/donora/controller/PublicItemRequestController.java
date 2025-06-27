package com.donora.controller;

import com.donora.dto.ItemRequestResponse;
import com.donora.service.ItemRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/emergency-needs")
public class PublicItemRequestController {

    @Autowired
    private ItemRequestService itemRequestService;

    @GetMapping
    public ResponseEntity<List<ItemRequestResponse>> getAllEmergencyNeeds() {
        List<ItemRequestResponse> emergencies = itemRequestService.getAllEmergencyNeeds();
        return ResponseEntity.ok(emergencies);
    }
}
