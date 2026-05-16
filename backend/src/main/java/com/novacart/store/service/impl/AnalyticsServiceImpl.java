package com.novacart.store.service.impl;

import com.novacart.store.dto.AdminAnalyticsResponse;
import com.novacart.store.entity.CustomerOrder;
import com.novacart.store.entity.OrderItem;
import com.novacart.store.entity.OrderStatus;
import com.novacart.store.entity.Product;
import com.novacart.store.repository.CustomerOrderRepository;
import com.novacart.store.repository.CustomerProfileRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.repository.RefundRequestRepository;
import com.novacart.store.service.AnalyticsService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    private final CustomerOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RefundRequestRepository refundRequestRepository;
    private final CustomerProfileRepository customerProfileRepository;

    public AnalyticsServiceImpl(
            CustomerOrderRepository orderRepository,
            ProductRepository productRepository,
            RefundRequestRepository refundRequestRepository,
            CustomerProfileRepository customerProfileRepository
    ) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.customerProfileRepository = customerProfileRepository;
    }

    @Override
    public AdminAnalyticsResponse getAnalytics() {
        List<CustomerOrder> orders = orderRepository.findAllByOrderByCreatedAtDesc();
        List<CustomerOrder> revenueOrders = orders.stream()
                .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
                .toList();
        Instant now = Instant.now();
        BigDecimal totalRevenue = revenueOrders.stream()
                .map(CustomerOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        long totalOrders = revenueOrders.size();

        return new AdminAnalyticsResponse(
                sumSince(revenueOrders, now.minus(1, ChronoUnit.DAYS)),
                sumSince(revenueOrders, now.minus(7, ChronoUnit.DAYS)),
                sumSince(revenueOrders, now.minus(30, ChronoUnit.DAYS)),
                sumSince(revenueOrders, now.minus(365, ChronoUnit.DAYS)),
                totalRevenue,
                totalOrders,
                totalOrders == 0 ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : totalRevenue.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP),
                orderRepository.countByStatus(OrderStatus.PENDING),
                refundRequestRepository.count(),
                productRepository.countByStockQuantityLessThanEqual(DEFAULT_LOW_STOCK_THRESHOLD),
                countRepeatCustomers(revenueOrders),
                trend(revenueOrders),
                topRegions(revenueOrders),
                customerPreferences(revenueOrders),
                bestSellingProducts(revenueOrders)
        );
    }

    private BigDecimal sumSince(List<CustomerOrder> orders, Instant start) {
        return orders.stream()
                .filter(order -> order.getCreatedAt() != null && !order.getCreatedAt().isBefore(start))
                .map(CustomerOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private long countRepeatCustomers(List<CustomerOrder> orders) {
        Map<String, Long> ordersByEmail = new HashMap<>();
        for (CustomerOrder order : orders) {
            ordersByEmail.merge(order.getCustomerEmail().toLowerCase(), 1L, Long::sum);
        }
        long observedRepeatCustomers = ordersByEmail.values().stream().filter(count -> count > 1).count();
        return Math.max(observedRepeatCustomers, customerProfileRepository.countByLastOrderAtNotNull() - ordersByEmail.size());
    }

    private List<AdminAnalyticsResponse.TrendPoint> trend(List<CustomerOrder> orders) {
        LocalDate today = LocalDate.now(ZoneOffset.UTC);
        Map<LocalDate, RevenueCounter> byDate = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            byDate.put(today.minusDays(i), new RevenueCounter());
        }
        for (CustomerOrder order : orders) {
            if (order.getCreatedAt() == null) {
                continue;
            }
            LocalDate date = order.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate();
            RevenueCounter counter = byDate.get(date);
            if (counter != null) {
                counter.revenue = counter.revenue.add(order.getTotalAmount());
                counter.orders++;
            }
        }
        return byDate.entrySet().stream()
                .map(entry -> new AdminAnalyticsResponse.TrendPoint(
                        entry.getKey(),
                        entry.getValue().revenue.setScale(2, RoundingMode.HALF_UP),
                        entry.getValue().orders
                ))
                .toList();
    }

    private List<AdminAnalyticsResponse.LabelMetric> topRegions(List<CustomerOrder> orders) {
        Map<String, RevenueCounter> regions = new HashMap<>();
        for (CustomerOrder order : orders) {
            String label = order.getRegion() == null || order.getRegion().isBlank()
                    ? order.getCountry()
                    : order.getRegion() + ", " + order.getCountry();
            RevenueCounter counter = regions.computeIfAbsent(label, ignored -> new RevenueCounter());
            counter.orders++;
            counter.revenue = counter.revenue.add(order.getTotalAmount());
        }
        return regions.entrySet().stream()
                .sorted(Map.Entry.<String, RevenueCounter>comparingByValue(Comparator.comparing(counter -> counter.revenue)).reversed())
                .limit(5)
                .map(entry -> new AdminAnalyticsResponse.LabelMetric(entry.getKey(), entry.getValue().orders, entry.getValue().revenue.setScale(2, RoundingMode.HALF_UP)))
                .toList();
    }

    private List<AdminAnalyticsResponse.LabelMetric> customerPreferences(List<CustomerOrder> orders) {
        Map<String, RevenueCounter> preferences = new HashMap<>();
        Map<Long, Product> productCache = new HashMap<>();
        for (CustomerOrder order : orders) {
            for (OrderItem item : order.getItems()) {
                Product product = productCache.computeIfAbsent(item.getProductId(), id -> productRepository.findById(id).orElse(null));
                if (product != null) {
                    addPreference(preferences, "Category: " + product.getCategory().getName(), item);
                }
                if (item.getSelectedColor() != null) {
                    addPreference(preferences, "Color: " + item.getSelectedColor(), item);
                }
                if (item.getSelectedSize() != null) {
                    addPreference(preferences, "Size: " + item.getSelectedSize(), item);
                }
            }
        }
        return preferences.entrySet().stream()
                .sorted(Map.Entry.<String, RevenueCounter>comparingByValue(Comparator.comparingLong(counter -> counter.orders)).reversed())
                .limit(8)
                .map(entry -> new AdminAnalyticsResponse.LabelMetric(entry.getKey(), entry.getValue().orders, entry.getValue().revenue.setScale(2, RoundingMode.HALF_UP)))
                .toList();
    }

    private void addPreference(Map<String, RevenueCounter> preferences, String label, OrderItem item) {
        RevenueCounter counter = preferences.computeIfAbsent(label, ignored -> new RevenueCounter());
        counter.orders += item.getQuantity();
        counter.revenue = counter.revenue.add(item.getLineTotal());
    }

    private List<AdminAnalyticsResponse.ProductMetric> bestSellingProducts(List<CustomerOrder> orders) {
        Map<Long, ProductCounter> products = new HashMap<>();
        for (CustomerOrder order : orders) {
            for (OrderItem item : order.getItems()) {
                ProductCounter counter = products.computeIfAbsent(item.getProductId(), id -> new ProductCounter(item.getProductId(), item.getProductName()));
                counter.unitsSold += item.getQuantity();
                counter.revenue = counter.revenue.add(item.getLineTotal());
            }
        }
        return products.values().stream()
                .sorted(Comparator.comparingLong((ProductCounter counter) -> counter.unitsSold).reversed())
                .limit(5)
                .map(counter -> new AdminAnalyticsResponse.ProductMetric(counter.productId, counter.productName, counter.unitsSold, counter.revenue.setScale(2, RoundingMode.HALF_UP)))
                .toList();
    }

    private static final class RevenueCounter {
        private long orders;
        private BigDecimal revenue = BigDecimal.ZERO;
    }

    private static final class ProductCounter {
        private final Long productId;
        private final String productName;
        private long unitsSold;
        private BigDecimal revenue = BigDecimal.ZERO;

        private ProductCounter(Long productId, String productName) {
            this.productId = productId;
            this.productName = productName;
        }
    }
}
