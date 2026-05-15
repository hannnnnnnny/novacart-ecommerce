package com.novacart.store.service.impl;

import com.novacart.store.dto.CheckoutItemRequest;
import com.novacart.store.dto.CheckoutRequest;
import com.novacart.store.dto.OrderItemResponse;
import com.novacart.store.dto.OrderResponse;
import com.novacart.store.entity.CustomerOrder;
import com.novacart.store.entity.OrderItem;
import com.novacart.store.entity.OrderStatus;
import com.novacart.store.entity.PaymentStatus;
import com.novacart.store.entity.Product;
import com.novacart.store.entity.ShippingMethod;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.CustomerOrderRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.service.OrderService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Map<OrderStatus, List<OrderStatus>> ALLOWED_STATUS_TRANSITIONS = buildStatusTransitions();
    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");

    private final CustomerOrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(CustomerOrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public OrderResponse createOrder(CheckoutRequest request) {
        String idempotencyKey = normalizeIdempotencyKey(request.idempotencyKey());
        if (idempotencyKey != null) {
            var existingOrder = orderRepository.findByIdempotencyKey(idempotencyKey);
            if (existingOrder.isPresent()) {
                return toResponse(existingOrder.get());
            }
        }

        validateDemoPayment(request);
        Map<Long, Integer> quantitiesByProduct = aggregateQuantities(request.items());
        ShippingMethod shippingMethod = resolveShippingMethod(request.shippingMethod());

        CustomerOrder order = new CustomerOrder(
                request.customerName().trim(),
                request.customerEmail().trim(),
                request.shippingAddress().trim(),
                request.city().trim(),
                request.postalCode().trim(),
                request.country().trim()
        );
        order.setIdempotencyKey(idempotencyKey);
        order.setShippingMethod(shippingMethod);
        order.setPaymentMethod(normalizePaymentMethod(request.paymentMethod()));
        order.setPaymentStatus(PaymentStatus.PAID);
        order.setStatus(OrderStatus.PAID);

        for (Map.Entry<Long, Integer> entry : quantitiesByProduct.entrySet()) {
            Product product = productRepository.findByIdForUpdate(entry.getKey())
                    .filter(Product::isStorefrontVisible)
                    .orElseThrow(() -> new ResourceNotFoundException("Product was not found."));

            int requestedQuantity = entry.getValue();
            if (product.getStockQuantity() < requestedQuantity) {
                throw new BusinessRuleException("Insufficient stock for " + product.getName() + ".");
            }

            product.setStockQuantity(product.getStockQuantity() - requestedQuantity);
            order.addItem(new OrderItem(product.getId(), product.getName(), product.getPrice(), requestedQuantity));
        }

        order.applyTotals(
                shippingAmount(shippingMethod),
                taxAmount(order.getSubtotalAmount()),
                BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        );

        CustomerOrder savedOrder = orderRepository.save(order);
        savedOrder.setOrderNumber(generateOrderNumber(savedOrder.getId()));
        return toResponse(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrder(Long id) {
        CustomerOrder order = findOrderWithItems(id);
        return toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAdminOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public OrderResponse updateStatus(Long id, OrderStatus status) {
        CustomerOrder order = findOrderWithItems(id);
        validateStatusTransition(order.getStatus(), status);
        order.setStatus(status);
        return toResponse(order);
    }

    private static Map<OrderStatus, List<OrderStatus>> buildStatusTransitions() {
        Map<OrderStatus, List<OrderStatus>> transitions = new EnumMap<>(OrderStatus.class);
        transitions.put(OrderStatus.PENDING, List.of(OrderStatus.PAID, OrderStatus.PROCESSING, OrderStatus.CANCELLED));
        transitions.put(OrderStatus.PAID, List.of(OrderStatus.PROCESSING, OrderStatus.CANCELLED));
        transitions.put(OrderStatus.PROCESSING, List.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED));
        transitions.put(OrderStatus.SHIPPED, List.of(OrderStatus.COMPLETED));
        transitions.put(OrderStatus.COMPLETED, List.of());
        transitions.put(OrderStatus.CANCELLED, List.of());
        return Map.copyOf(transitions);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus nextStatus) {
        if (nextStatus == null) {
            throw new BusinessRuleException("Order status is required.");
        }

        if (currentStatus == nextStatus) {
            return;
        }

        List<OrderStatus> allowedStatuses = ALLOWED_STATUS_TRANSITIONS.getOrDefault(currentStatus, List.of());
        if (!allowedStatuses.contains(nextStatus)) {
            throw new BusinessRuleException("Order status cannot change from "
                    + formatStatus(currentStatus) + " to " + formatStatus(nextStatus) + ".");
        }
    }

    private String formatStatus(OrderStatus status) {
        String label = status.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        return Character.toUpperCase(label.charAt(0)) + label.substring(1);
    }

    private void validateDemoPayment(CheckoutRequest request) {
        String paymentMethod = normalizePaymentMethod(request.paymentMethod());
        if (Boolean.TRUE.equals(request.simulatePaymentFailure()) || "Demo Card Declined".equalsIgnoreCase(paymentMethod)) {
            throw new BusinessRuleException("Demo payment was declined. Select Demo Card Approved and try again.");
        }
    }

    private ShippingMethod resolveShippingMethod(String shippingMethod) {
        if (shippingMethod == null || shippingMethod.isBlank()) {
            return ShippingMethod.STANDARD;
        }
        try {
            return ShippingMethod.valueOf(shippingMethod.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleException("Select a valid shipping method.");
        }
    }

    private String normalizePaymentMethod(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isBlank()) {
            return "Demo Card Approved";
        }
        return paymentMethod.trim();
    }

    private String normalizeIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            return null;
        }
        return idempotencyKey.trim();
    }

    private BigDecimal shippingAmount(ShippingMethod shippingMethod) {
        return switch (shippingMethod) {
            case STANDARD -> new BigDecimal("6.00");
            case EXPRESS -> new BigDecimal("14.00");
            case PICKUP -> BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        };
    }

    private BigDecimal taxAmount(BigDecimal subtotalAmount) {
        return subtotalAmount.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
    }

    private String generateOrderNumber(Long id) {
        String datePart = LocalDate.now(ZoneOffset.UTC).format(DateTimeFormatter.BASIC_ISO_DATE);
        return "NC-" + datePart + "-" + String.format("%04d", id);
    }

    private Map<Long, Integer> aggregateQuantities(List<CheckoutItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new BusinessRuleException("Your cart must include at least one item.");
        }

        Map<Long, Integer> quantitiesByProduct = new LinkedHashMap<>();
        for (CheckoutItemRequest item : items) {
            if (item.productId() == null || item.productId() <= 0) {
                throw new BusinessRuleException("Product ID must be positive.");
            }
            if (item.quantity() < 1) {
                throw new BusinessRuleException("Quantity must be at least 1.");
            }
            quantitiesByProduct.merge(item.productId(), item.quantity(), Integer::sum);
        }
        return quantitiesByProduct;
    }

    private CustomerOrder findOrderWithItems(Long id) {
        return orderRepository.findWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order was not found."));
    }

    private OrderResponse toResponse(CustomerOrder order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProductId(),
                        item.getProductName(),
                        item.getUnitPrice(),
                        item.getQuantity(),
                        item.getLineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerName(),
                order.getCustomerEmail(),
                order.getShippingAddress(),
                order.getCity(),
                order.getPostalCode(),
                order.getCountry(),
                order.getShippingMethod(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getStatus(),
                order.getSubtotalAmount(),
                order.getShippingAmount(),
                order.getTaxAmount(),
                order.getDiscountAmount(),
                order.getTotalAmount(),
                items,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
