package com.donora.controller;

import com.donora.dto.BusinessProfileRequest;
import com.donora.dto.BusinessProfileResponse;
import com.donora.service.BusinessProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/business/profile")
public class BusinessProfileController {

    @Autowired
    private BusinessProfileService businessProfileService;

    @PostMapping
    public BusinessProfileResponse createOrUpdateProfile(@RequestBody BusinessProfileRequest request, Principal principal) {
        return businessProfileService.saveOrUpdateProfile(request, principal.getName());
    }

    @GetMapping
    public BusinessProfileResponse getProfile(Principal principal) {
        return businessProfileService.getBusinessProfile(principal.getName());
    }
}
