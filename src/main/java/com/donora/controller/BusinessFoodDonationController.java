package com.donora.controller;

import com.donora.dto.BusinessImpactResponse;
import com.donora.dto.FoodDonationRequest;
import com.donora.dto.FoodDonationResponse;
import com.donora.service.FoodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/business/food-donations")
@PreAuthorize("hasRole('BUSINESS')")
public class BusinessFoodDonationController {

    @Autowired
    private FoodDonationService foodDonationService;

    // POST: Business user submits food donation
    @PostMapping
    public ResponseEntity<String> donateFood(@RequestBody FoodDonationRequest request, Principal principal) {
        foodDonationService.createFoodDonation(principal.getName(), request);
        return ResponseEntity.ok("Food donation submitted successfully.");
    }

    // GET: Business user views their food donation history
    @GetMapping("/history")
    public ResponseEntity<List<FoodDonationResponse>> getMyFoodDonations(Principal principal) {
        List<FoodDonationResponse> donations = foodDonationService.getFoodDonationsForBusiness(principal.getName());
        return ResponseEntity.ok(donations);
    }

    @GetMapping("/my-impact")
    public ResponseEntity<BusinessImpactResponse> getBusinessImpact(Principal principal) {
        BusinessImpactResponse impact = foodDonationService.getBusinessImpact(principal.getName());
        return ResponseEntity.ok(impact);
    }

}
