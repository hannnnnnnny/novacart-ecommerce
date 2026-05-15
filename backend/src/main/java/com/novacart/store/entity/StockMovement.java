package com.novacart.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false, length = 180)
    private String productName;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private StockMovementType type;

    @Column(nullable = false)
    private int quantityChange;

    @Column(nullable = false)
    private int stockAfter;

    @Column(nullable = false, length = 240)
    private String reason;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected StockMovement() {
    }

    public StockMovement(
            Long productId,
            String productName,
            Long orderId,
            StockMovementType type,
            int quantityChange,
            int stockAfter,
            String reason
    ) {
        this.productId = productId;
        this.productName = productName;
        this.orderId = orderId;
        this.type = type;
        this.quantityChange = quantityChange;
        this.stockAfter = stockAfter;
        this.reason = reason;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Long getOrderId() {
        return orderId;
    }

    public StockMovementType getType() {
        return type;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public int getStockAfter() {
        return stockAfter;
    }

    public String getReason() {
        return reason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
