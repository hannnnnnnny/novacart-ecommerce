package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.RefundRequestResponse;
import com.novacart.store.dto.RefundStatusUpdateRequest;
import com.novacart.store.entity.RefundStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/refunds")
@Validated
public class AdminRefundController {

    private final CustomerCareService customerCareService;

    public AdminRefundController(CustomerCareService customerCareService) {
        this.customerCareService = customerCareService;
    }

    @GetMapping
    public ApiResponse<List<RefundRequestResponse>> findRefunds(@RequestParam(required = false) RefundStatus status) {
        return ApiResponse.success("Refund requests loaded successfully.", customerCareService.findRefundRequests(status));
    }

    @PatchMapping("/{id}")
    public ApiResponse<RefundRequestResponse> updateRefund(
            @Positive(message = "Refund request ID must be positive.")
            @PathVariable Long id,
            @Valid @RequestBody RefundStatusUpdateRequest request
    ) {
        return ApiResponse.success("Refund request updated successfully.", customerCareService.updateRefundRequest(id, request));
    }
}
