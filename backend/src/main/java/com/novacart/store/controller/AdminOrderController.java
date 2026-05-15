package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.OrderResponse;
import com.novacart.store.dto.OrderStatusUpdateRequest;
import com.novacart.store.service.OrderService;
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
@RequestMapping("/api/admin/orders")
@Validated
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> findOrders() {
        return ApiResponse.success("Orders loaded successfully.", orderService.findAdminOrders());
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> findOrder(
            @Positive(message = "Order ID must be positive.")
            @PathVariable Long id
    ) {
        return ApiResponse.success("Order loaded successfully.", orderService.findOrder(id));
    }

    @PatchMapping("/{id}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @Positive(message = "Order ID must be positive.")
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request
    ) {
        return ApiResponse.success("Order status updated successfully.", orderService.updateStatus(id, request.status()));
    }
}
