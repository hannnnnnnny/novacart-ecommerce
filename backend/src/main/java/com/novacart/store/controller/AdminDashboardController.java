package com.novacart.store.controller;

import com.novacart.store.dto.ApiResponse;
import com.novacart.store.dto.DashboardMetricsResponse;
import com.novacart.store.dto.InventoryWarningResponse;
import com.novacart.store.service.AdminDashboardService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Validated
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @GetMapping("/dashboard/metrics")
    public ApiResponse<DashboardMetricsResponse> getMetrics() {
        return ApiResponse.success("Dashboard metrics loaded successfully.", adminDashboardService.getMetrics());
    }

    @GetMapping("/inventory/warnings")
    public ApiResponse<List<InventoryWarningResponse>> getInventoryWarnings(
            @Min(value = 0, message = "Inventory threshold cannot be negative.")
            @Max(value = 10_000, message = "Inventory threshold must be 10,000 or fewer.")
            @RequestParam(defaultValue = "5") int threshold
    ) {
        return ApiResponse.success("Inventory warnings loaded successfully.", adminDashboardService.getInventoryWarnings(threshold));
    }
}
