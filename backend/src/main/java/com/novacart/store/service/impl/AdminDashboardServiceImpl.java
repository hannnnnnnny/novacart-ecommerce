package com.novacart.store.service.impl;

import com.novacart.store.dto.DashboardMetricsResponse;
import com.novacart.store.dto.InventoryAdjustmentRequest;
import com.novacart.store.dto.InventoryWarningResponse;
import com.novacart.store.dto.StockMovementResponse;
import com.novacart.store.entity.OrderStatus;
import com.novacart.store.entity.Product;
import com.novacart.store.entity.ProductStatus;
import com.novacart.store.entity.StockMovement;
import com.novacart.store.entity.StockMovementType;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.CategoryRepository;
import com.novacart.store.repository.CustomerOrderRepository;
import com.novacart.store.repository.ProductRepository;
import com.novacart.store.repository.StockMovementRepository;
import com.novacart.store.service.AdminDashboardService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final CustomerOrderRepository orderRepository;
    private final StockMovementRepository stockMovementRepository;

    public AdminDashboardServiceImpl(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            CustomerOrderRepository orderRepository,
            StockMovementRepository stockMovementRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.stockMovementRepository = stockMovementRepository;
    }

    @Override
    public DashboardMetricsResponse getMetrics() {
        return new DashboardMetricsResponse(
                productRepository.count(),
                productRepository.countByActiveTrueAndStatus(ProductStatus.ACTIVE),
                categoryRepository.count(),
                orderRepository.count(),
                orderRepository.countByStatus(OrderStatus.PENDING),
                productRepository.countByStockQuantityLessThanEqual(DEFAULT_LOW_STOCK_THRESHOLD),
                orderRepository.sumTotalAmountByStatusNot(OrderStatus.CANCELLED)
        );
    }

    @Override
    public List<InventoryWarningResponse> getInventoryWarnings(int threshold) {
        return productRepository.findAllByStockQuantityLessThanEqualOrderByStockQuantityAsc(threshold)
                .stream()
                .map(this::toWarning)
                .toList();
    }

    @Override
    public List<StockMovementResponse> getRecentStockMovements() {
        return stockMovementRepository.findTop20ByOrderByCreatedAtDesc()
                .stream()
                .map(this::toStockMovementResponse)
                .toList();
    }

    @Override
    @Transactional
    public StockMovementResponse adjustInventory(InventoryAdjustmentRequest request) {
        if (request.quantityChange() == 0) {
            throw new BusinessRuleException("Quantity change cannot be zero.");
        }

        Product product = productRepository.findByIdForUpdate(request.productId())
                .orElseThrow(() -> new ResourceNotFoundException("Product was not found."));
        int stockAfter = product.getStockQuantity() + request.quantityChange();
        if (stockAfter < 0) {
            throw new BusinessRuleException("Inventory adjustment cannot reduce stock below zero.");
        }

        product.setStockQuantity(stockAfter);
        StockMovement movement = stockMovementRepository.save(new StockMovement(
                product.getId(),
                product.getName(),
                null,
                StockMovementType.MANUAL_ADJUSTMENT,
                request.quantityChange(),
                stockAfter,
                request.reason().trim()
        ));
        return toStockMovementResponse(movement);
    }

    private InventoryWarningResponse toWarning(Product product) {
        return new InventoryWarningResponse(
                product.getId(),
                product.getName(),
                product.getCategory().getName(),
                product.getStockQuantity(),
                product.getLowStockThreshold(),
                product.getStatus(),
                product.isActive()
        );
    }

    private StockMovementResponse toStockMovementResponse(StockMovement movement) {
        return new StockMovementResponse(
                movement.getId(),
                movement.getProductId(),
                movement.getProductName(),
                movement.getOrderId(),
                movement.getType(),
                movement.getQuantityChange(),
                movement.getStockAfter(),
                movement.getReason(),
                movement.getCreatedAt()
        );
    }
}
