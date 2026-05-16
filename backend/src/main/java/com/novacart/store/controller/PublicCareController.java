package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.RefundRequestCreateRequest;
import com.novacart.store.dto.RefundRequestResponse;
import com.novacart.store.dto.SupportTicketRequest;
import com.novacart.store.dto.SupportTicketResponse;
import com.novacart.store.service.CustomerCareService;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
@Validated
public class PublicCareController {

    private final CustomerCareService customerCareService;

    public PublicCareController(CustomerCareService customerCareService) {
        this.customerCareService = customerCareService;
    }

    @PostMapping("/support-tickets")
    public ApiResponse<SupportTicketResponse> createSupportTicket(@Valid @RequestBody SupportTicketRequest request) {
        return ApiResponse.success("Support ticket submitted successfully.", customerCareService.createSupportTicket(request));
    }

    @PostMapping("/refunds")
    public ApiResponse<RefundRequestResponse> createRefundRequest(@Valid @RequestBody RefundRequestCreateRequest request) {
        return ApiResponse.success("Refund request submitted successfully.", customerCareService.createRefundRequest(request));
    }
}
