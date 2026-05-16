package com.novacart.store.service.impl;

import com.novacart.store.dto.RefundRequestCreateRequest;
import com.novacart.store.dto.RefundRequestResponse;
import com.novacart.store.dto.RefundStatusUpdateRequest;
import com.novacart.store.dto.SupportTicketRequest;
import com.novacart.store.dto.SupportTicketResponse;
import com.novacart.store.dto.SupportTicketUpdateRequest;
import com.novacart.store.entity.CustomerOrder;
import com.novacart.store.entity.PaymentStatus;
import com.novacart.store.entity.RefundRequest;
import com.novacart.store.entity.RefundStatus;
import com.novacart.store.entity.SupportTicket;
import com.novacart.store.exception.BusinessRuleException;
import com.novacart.store.exception.ResourceNotFoundException;
import com.novacart.store.repository.CustomerOrderRepository;
import com.novacart.store.repository.RefundRequestRepository;
import com.novacart.store.repository.SupportTicketRepository;
import com.novacart.store.service.CustomerCareService;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CustomerCareServiceImpl implements CustomerCareService {

    private static final Duration REFUND_WINDOW = Duration.ofDays(30);
    private static final List<RefundStatus> OPEN_REFUND_STATUSES = List.of(
            RefundStatus.REQUESTED,
            RefundStatus.UNDER_REVIEW,
            RefundStatus.APPROVED
    );

    private final SupportTicketRepository supportTicketRepository;
    private final RefundRequestRepository refundRequestRepository;
    private final CustomerOrderRepository orderRepository;

    public CustomerCareServiceImpl(
            SupportTicketRepository supportTicketRepository,
            RefundRequestRepository refundRequestRepository,
            CustomerOrderRepository orderRepository
    ) {
        this.supportTicketRepository = supportTicketRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public SupportTicketResponse createSupportTicket(SupportTicketRequest request) {
        SupportTicket ticket = new SupportTicket(
                request.issueType(),
                clean(request.orderNumber()),
                request.email().trim(),
                request.customerName().trim(),
                request.message().trim()
        );
        return toSupportResponse(supportTicketRepository.save(ticket));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupportTicketResponse> findSupportTickets() {
        return supportTicketRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toSupportResponse)
                .toList();
    }

    @Override
    public SupportTicketResponse updateSupportTicket(Long id, SupportTicketUpdateRequest request) {
        SupportTicket ticket = supportTicketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Support ticket was not found."));
        ticket.setStatus(request.status());
        ticket.setInternalNotes(clean(request.internalNotes()));
        return toSupportResponse(ticket);
    }

    @Override
    public RefundRequestResponse createRefundRequest(RefundRequestCreateRequest request) {
        CustomerOrder order = orderRepository.findByOrderNumberAndCustomerEmailIgnoreCase(
                        request.orderNumber().trim(),
                        request.email().trim()
                )
                .orElseThrow(() -> new ResourceNotFoundException("Order was not found for that email address."));
        validateRefundRequest(order);
        RefundRequest refundRequest = new RefundRequest(order, request.email().trim(), request.reason().trim());
        order.setRefundStatus(RefundStatus.REQUESTED);
        return toRefundResponse(refundRequestRepository.save(refundRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundRequestResponse> findRefundRequests(RefundStatus status) {
        List<RefundRequest> refunds = status == null
                ? refundRequestRepository.findAllByOrderByCreatedAtDesc()
                : refundRequestRepository.findAllByStatusOrderByCreatedAtDesc(status);
        return refunds.stream().map(this::toRefundResponse).toList();
    }

    @Override
    public RefundRequestResponse updateRefundRequest(Long id, RefundStatusUpdateRequest request) {
        RefundRequest refundRequest = refundRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Refund request was not found."));
        CustomerOrder order = refundRequest.getOrder();
        refundRequest.setStatus(request.status());
        refundRequest.setInternalNotes(clean(request.internalNotes()));
        order.setRefundStatus(request.status());
        if (request.status() == RefundStatus.APPROVED || request.status() == RefundStatus.REFUNDED) {
            order.setPaymentStatus(PaymentStatus.REFUNDED);
        }
        return toRefundResponse(refundRequest);
    }

    private void validateRefundRequest(CustomerOrder order) {
        if (order.getPaymentStatus() != PaymentStatus.PAID) {
            throw new BusinessRuleException("Only paid orders can enter the refund workflow.");
        }
        if (order.getCreatedAt() != null && order.getCreatedAt().isBefore(Instant.now().minus(REFUND_WINDOW))) {
            throw new BusinessRuleException("This order is outside the 30-day refund window.");
        }
        if (refundRequestRepository.existsByOrderIdAndStatusIn(order.getId(), OPEN_REFUND_STATUSES)) {
            throw new BusinessRuleException("This order already has an open refund request.");
        }
    }

    private SupportTicketResponse toSupportResponse(SupportTicket ticket) {
        return new SupportTicketResponse(
                ticket.getId(),
                ticket.getIssueType(),
                ticket.getOrderNumber(),
                ticket.getEmail(),
                ticket.getCustomerName(),
                ticket.getMessage(),
                ticket.getStatus(),
                ticket.getInternalNotes(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    private RefundRequestResponse toRefundResponse(RefundRequest refundRequest) {
        CustomerOrder order = refundRequest.getOrder();
        return new RefundRequestResponse(
                refundRequest.getId(),
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerName(),
                refundRequest.getEmail(),
                refundRequest.getReason(),
                refundRequest.getStatus(),
                refundRequest.getInternalNotes(),
                refundRequest.getCreatedAt(),
                refundRequest.getUpdatedAt()
        );
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
