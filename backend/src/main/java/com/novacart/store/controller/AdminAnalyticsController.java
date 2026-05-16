package com.novacart.store.controller;

import com.novacart.store.dto.AdminAnalyticsResponse;
import com.novacart.store.dto.ApiResponse;
import com.novacart.store.service.AnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/analytics")
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    public AdminAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ApiResponse<AdminAnalyticsResponse> getAnalytics() {
        return ApiResponse.success("Analytics loaded successfully.", analyticsService.getAnalytics());
    }
}
