package com.donora.controller;

import com.donora.dto.ItemDonationResponse;
import com.donora.dto.UpdateDonationStatusRequest;
import com.donora.service.ItemDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ngos/item-donations")
@PreAuthorize("hasRole('NGO')")
public class ItemDonationController {

    @Autowired
    private ItemDonationService itemDonationService;

    @GetMapping
    public ResponseEntity<List<ItemDonationResponse>> getItemDonations(Principal principal) {
        List<ItemDonationResponse> donations = itemDonationService.getItemDonationsForNgo(principal.getName());
        return ResponseEntity.ok(donations);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateItemDonationStatus(
            @PathVariable Long id,
            @RequestBody UpdateDonationStatusRequest request,
            Principal principal) {

        itemDonationService.updateItemDonationStatus(id, principal.getName(), request.getStatus());
        return ResponseEntity.ok("Item donation status updated successfully.");
    }
}
