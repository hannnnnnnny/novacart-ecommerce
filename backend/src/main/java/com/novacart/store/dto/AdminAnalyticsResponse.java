package com.novacart.store.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record AdminAnalyticsResponse(
        BigDecimal dailySales,
        BigDecimal weeklySales,
        BigDecimal monthlySales,
        BigDecimal yearlySales,
        BigDecimal totalRevenue,
        long totalOrders,
        BigDecimal averageOrderValue,
        long pendingOrders,
        long refundRequests,
        long lowStockProducts,
        long repeatCustomers,
        List<TrendPoint> salesTrend,
        List<LabelMetric> topRegions,
        List<LabelMetric> customerPreferenceOverview,
        List<ProductMetric> bestSellingProducts
) {
    public record TrendPoint(LocalDate date, BigDecimal revenue, long orders) {
    }

    public record LabelMetric(String label, long count, BigDecimal revenue) {
    }

    public record ProductMetric(Long productId, String productName, long unitsSold, BigDecimal revenue) {
    }
}
