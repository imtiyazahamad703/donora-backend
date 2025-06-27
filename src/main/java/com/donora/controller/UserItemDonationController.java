package com.donora.controller;

import com.donora.dto.ItemDonationRequest;
import com.donora.service.ItemDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users/item-donations")
@PreAuthorize("hasRole('USER')")
public class UserItemDonationController {

    @Autowired
    private ItemDonationService itemDonationService;

    @PostMapping
    public ResponseEntity<String> donateItem(@RequestBody ItemDonationRequest request, Principal principal) {
        itemDonationService.createItemDonation(principal.getName(), request);
        return ResponseEntity.ok("Item donation submitted successfully.");
    }
}
