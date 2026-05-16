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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "support_tickets")
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SupportIssueType issueType = SupportIssueType.OTHER;

    @Column(length = 32)
    private String orderNumber;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(nullable = false, length = 140)
    private String customerName;

    @Column(nullable = false, length = 2_000)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private SupportTicketStatus status = SupportTicketStatus.OPEN;

    @Column(length = 1_000)
    private String internalNotes;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    protected SupportTicket() {
    }

    public SupportTicket(SupportIssueType issueType, String orderNumber, String email, String customerName, String message) {
        this.issueType = issueType == null ? SupportIssueType.OTHER : issueType;
        this.orderNumber = orderNumber;
        this.email = email;
        this.customerName = customerName;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public SupportIssueType getIssueType() {
        return issueType;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMessage() {
        return message;
    }

    public SupportTicketStatus getStatus() {
        return status;
    }

    public void setStatus(SupportTicketStatus status) {
        this.status = status;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
