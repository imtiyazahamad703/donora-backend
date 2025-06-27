package com.donora.controller;

import com.donora.dto.FoodDonationResponse;
import com.donora.dto.UpdateDonationStatusRequest;
import com.donora.service.FoodDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ngos/food-donations")
@PreAuthorize("hasRole('NGO')")
public class FoodDonationController {

    @Autowired
    private FoodDonationService foodDonationService;

    // GET: View incoming food donations for logged-in NGO
    @GetMapping
    public ResponseEntity<List<FoodDonationResponse>> getFoodDonations(Principal principal) {
        List<FoodDonationResponse> donations = foodDonationService.getFoodDonationsForNgo(principal.getName());
        return ResponseEntity.ok(donations);
    }

    // PUT: Update status of a specific food donation (ACCEPTED/REJECTED)
    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateFoodDonationStatus(
            @PathVariable Long id,
            @RequestBody UpdateDonationStatusRequest request,
            Principal principal) {

        foodDonationService.updateFoodDonationStatus(id, principal.getName(), request.getStatus());
        return ResponseEntity.ok("Food donation status updated successfully.");
    }
}
