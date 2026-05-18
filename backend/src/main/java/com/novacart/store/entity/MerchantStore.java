package com.novacart.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "merchant_stores")
public class MerchantStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_account_id", nullable = false)
    private MerchantAccount merchantAccount;

    @Column(nullable = false, length = 140)
    private String name;

    @Column(nullable = false, unique = true, length = 160)
    private String slug;

    @Column(nullable = false, length = 80)
    private String category;

    @Column(nullable = false, length = 800)
    private String description;

    @Column(nullable = false, length = 80)
    private String templateKey;

    @Column(nullable = false, length = 20)
    private String brandColor;

    @Column(nullable = false, length = 8)
    private String logoText;

    @Column(nullable = false, length = 12)
    private String currency;

    @Column(nullable = false, length = 220)
    private String shippingMessage;

    @Column(nullable = false, length = 220)
    private String announcement;

    @Column(nullable = false)
    private boolean published;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    protected MerchantStore() {
    }

    public MerchantStore(
            MerchantAccount merchantAccount,
            String name,
            String slug,
            String category,
            String description,
            String templateKey,
            String brandColor,
            String logoText,
            String currency,
            String shippingMessage,
            String announcement,
            boolean published
    ) {
        this.merchantAccount = merchantAccount;
        this.name = name;
        this.slug = slug;
        this.category = category;
        this.description = description;
        this.templateKey = templateKey;
        this.brandColor = brandColor;
        this.logoText = logoText;
        this.currency = currency;
        this.shippingMessage = shippingMessage;
        this.announcement = announcement;
        this.published = published;
    }

    public Long getId() {
        return id;
    }

    public MerchantAccount getMerchantAccount() {
        return merchantAccount;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getTemplateKey() {
        return templateKey;
    }

    public String getBrandColor() {
        return brandColor;
    }

    public String getLogoText() {
        return logoText;
    }

    public String getCurrency() {
        return currency;
    }

    public String getShippingMessage() {
        return shippingMessage;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
