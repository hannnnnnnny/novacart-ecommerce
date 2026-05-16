package com.novacart.store.service.impl;

import com.novacart.store.dto.CustomerProfileResponse;
import com.novacart.store.entity.CustomerOrder;
import com.novacart.store.entity.CustomerProfile;
import com.novacart.store.entity.OrderStatus;
import com.novacart.store.repository.CustomerOrderRepository;
import com.novacart.store.repository.CustomerProfileRepository;
import com.novacart.store.service.CustomerService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerProfileRepository customerProfileRepository;
    private final CustomerOrderRepository orderRepository;

    public CustomerServiceImpl(
            CustomerProfileRepository customerProfileRepository,
            CustomerOrderRepository orderRepository
    ) {
        this.customerProfileRepository = customerProfileRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<CustomerProfileResponse> findCustomers() {
        Map<String, CustomerOrderSummary> summaries = buildOrderSummaries();
        return customerProfileRepository.findAllByOrderByLastOrderAtDescCreatedAtDesc()
                .stream()
                .map(profile -> toResponse(profile, summaries.getOrDefault(
                        normalizeEmail(profile.getEmail()),
                        new CustomerOrderSummary()
                )))
                .toList();
    }

    private Map<String, CustomerOrderSummary> buildOrderSummaries() {
        Map<String, CustomerOrderSummary> summaries = new HashMap<>();
        for (CustomerOrder order : orderRepository.findAllByOrderByCreatedAtDesc()) {
            if (order.getStatus() == OrderStatus.CANCELLED) {
                continue;
            }
            CustomerOrderSummary summary = summaries.computeIfAbsent(
                    normalizeEmail(order.getCustomerEmail()),
                    ignored -> new CustomerOrderSummary()
            );
            summary.orders++;
            summary.totalSpent = summary.totalSpent.add(order.getTotalAmount());
        }
        return summaries;
    }

    private CustomerProfileResponse toResponse(CustomerProfile profile, CustomerOrderSummary summary) {
        BigDecimal totalSpent = summary.totalSpent.setScale(2, RoundingMode.HALF_UP);
        BigDecimal averageOrderValue = summary.orders == 0
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : totalSpent.divide(BigDecimal.valueOf(summary.orders), 2, RoundingMode.HALF_UP);
        return new CustomerProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getEmail(),
                profile.getPhone(),
                profile.getAddressSummary(),
                profile.getCountry(),
                profile.getRegion(),
                profile.getCity(),
                summary.orders,
                totalSpent,
                averageOrderValue,
                profile.getCreatedAt(),
                profile.getLastOrderAt(),
                profile.getUpdatedAt()
        );
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    private static final class CustomerOrderSummary {
        private long orders;
        private BigDecimal totalSpent = BigDecimal.ZERO;
    }
}
