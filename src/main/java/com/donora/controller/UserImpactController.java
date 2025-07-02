package com.donora.controller;

import com.donora.dto.UserImpactResponse;
import com.donora.service.ItemDonationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('USER')")
public class UserImpactController {

    @Autowired
    private ItemDonationService itemDonationService;

    @GetMapping("/my-impact")
    public ResponseEntity<UserImpactResponse> getUserImpact(Principal principal) {
        UserImpactResponse impact = itemDonationService.getUserImpact(principal.getName());
        return ResponseEntity.ok(impact);
    }
}
