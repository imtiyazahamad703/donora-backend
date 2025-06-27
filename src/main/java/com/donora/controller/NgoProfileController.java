package com.donora.controller;

import com.donora.dto.NgoImpactStatsResponse;
import com.donora.dto.NgoProfileRequest;
import com.donora.dto.NgoProfileResponse;
import com.donora.service.NgoProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/ngos/profile")
@PreAuthorize("hasRole('NGO')")
public class NgoProfileController {

    @Autowired
    private NgoProfileService ngoProfileService;

    // Create or Update NGO profile
    @PostMapping
    public ResponseEntity<NgoProfileResponse> saveOrUpdateProfile(@RequestBody NgoProfileRequest request,
                                                                  Principal principal) {
        NgoProfileResponse response = ngoProfileService.saveOrUpdateProfile(request, principal.getName());
        return ResponseEntity.ok(response);
    }

    // Get NGO profile
    @GetMapping
    public ResponseEntity<NgoProfileResponse> getProfile(Principal principal) {
        NgoProfileResponse response = ngoProfileService.getNgoProfile(principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/impact")
    public ResponseEntity<NgoImpactStatsResponse> getImpactStats(Principal principal) {
        NgoImpactStatsResponse stats = ngoProfileService.getImpactStats(principal.getName());
        return ResponseEntity.ok(stats);
    }

}
