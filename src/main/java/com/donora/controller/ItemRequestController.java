package com.donora.controller;

import com.donora.dto.ItemRequestRequest;
import com.donora.dto.ItemRequestResponse;
import com.donora.service.ItemRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/ngos/needs")
@PreAuthorize("hasRole('NGO')")
public class ItemRequestController {

    @Autowired
    private ItemRequestService itemRequestService;


    // ✅ POST: Create item need
    @PostMapping
    public ResponseEntity<ItemRequestResponse> createNeed(@RequestBody ItemRequestRequest request,
                                                          Principal principal) {
        ItemRequestResponse response = itemRequestService.createItemRequest(request, principal.getName());
        return ResponseEntity.ok(response);
    }

    // ✅ GET: List all needs posted by this NGO
    @GetMapping
    public ResponseEntity<List<ItemRequestResponse>> getOwnNeeds(Principal principal) {
        List<ItemRequestResponse> needs = itemRequestService.getItemRequestsByNgo(principal.getName());
        return ResponseEntity.ok(needs);
    }

    // ✅ PUT: Mark an item need as emergency
    @PutMapping("/{id}/emergency")
    public ResponseEntity<String> markAsEmergency(@PathVariable Long id, Principal principal) {
        itemRequestService.markItemRequestAsEmergency(id, principal.getName());
        return ResponseEntity.ok("Item request marked as emergency.");
    }

}
