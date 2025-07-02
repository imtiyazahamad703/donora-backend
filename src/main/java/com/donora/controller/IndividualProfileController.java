package com.donora.controller;

import com.donora.dto.IndividualProfileRequest;
import com.donora.dto.IndividualProfileResponse;
import com.donora.service.IndividualProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/profile")
public class IndividualProfileController {

    @Autowired
    private IndividualProfileService profileService;

    @GetMapping
    public ResponseEntity<IndividualProfileResponse> getProfile(HttpServletRequest request) {
        String email = request.getUserPrincipal().getName();
        return ResponseEntity.ok(profileService.getMyProfile(email));
    }

    @PutMapping
    public ResponseEntity<IndividualProfileResponse> updateProfile(
            HttpServletRequest request,
            @RequestBody IndividualProfileRequest profileRequest
    ) {
        String email = request.getUserPrincipal().getName();
        return ResponseEntity.ok(profileService.updateMyProfile(email, profileRequest));
    }
}
