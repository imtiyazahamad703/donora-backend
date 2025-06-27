package com.donora.controller;

import com.donora.dto.FoodDonationRequest;
import com.donora.service.FoodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/business/food-donations")
@PreAuthorize("hasRole('BUSINESS')")
public class BusinessFoodDonationController {

    @Autowired
    private FoodDonationService foodDonationService;

    @PostMapping
    public ResponseEntity<String> donateFood(@RequestBody FoodDonationRequest request, Principal principal) {
        foodDonationService.createFoodDonation(principal.getName(), request);
        return ResponseEntity.ok("Food donation submitted successfully.");
    }
}
