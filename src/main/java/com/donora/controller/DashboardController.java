package com.donora.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    // Admin Dashboard - Accessible by Admin Role only
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    // NGO Dashboard - Accessible by NGO Role only
    @GetMapping("/ngo")
    @PreAuthorize("hasRole('NGO')")
    public String getNgoDashboard(Principal principal) {
        return "Welcome to the NGO Dashboard, " + principal.getName() + "!";
    }

    // User Dashboard - Accessible by User Role only
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String getUserDashboard(Principal principal) {
        return "Welcome to the User Dashboard, " + principal.getName() + "!";
    }
}
