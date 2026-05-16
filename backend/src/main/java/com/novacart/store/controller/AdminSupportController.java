package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.SupportTicketResponse;
import com.novacart.store.dto.SupportTicketUpdateRequest;
import com.novacart.store.service.CustomerCareService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/support-tickets")
@Validated
public class AdminSupportController {

    private final CustomerCareService customerCareService;

    public AdminSupportController(CustomerCareService customerCareService) {
        this.customerCareService = customerCareService;
    }

    @GetMapping
    public ApiResponse<List<SupportTicketResponse>> findSupportTickets() {
        return ApiResponse.success("Support tickets loaded successfully.", customerCareService.findSupportTickets());
    }

    @PatchMapping("/{id}")
    public ApiResponse<SupportTicketResponse> updateSupportTicket(
            @Positive(message = "Support ticket ID must be positive.")
            @PathVariable Long id,
            @Valid @RequestBody SupportTicketUpdateRequest request
    ) {
        return ApiResponse.success("Support ticket updated successfully.", customerCareService.updateSupportTicket(id, request));
    }
}
