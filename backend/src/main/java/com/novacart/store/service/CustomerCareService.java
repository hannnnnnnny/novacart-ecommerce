package com.novacart.store.service;

import com.novacart.store.dto.RefundRequestCreateRequest;
import com.novacart.store.dto.RefundRequestResponse;
import com.novacart.store.dto.RefundStatusUpdateRequest;
import com.novacart.store.dto.SupportTicketRequest;
import com.novacart.store.dto.SupportTicketResponse;
import com.novacart.store.dto.SupportTicketUpdateRequest;
import com.novacart.store.entity.RefundStatus;
import java.util.List;

public interface CustomerCareService {

    SupportTicketResponse createSupportTicket(SupportTicketRequest request);

    List<SupportTicketResponse> findSupportTickets();

    SupportTicketResponse updateSupportTicket(Long id, SupportTicketUpdateRequest request);

    RefundRequestResponse createRefundRequest(RefundRequestCreateRequest request);

    List<RefundRequestResponse> findRefundRequests(RefundStatus status);

    RefundRequestResponse updateRefundRequest(Long id, RefundStatusUpdateRequest request);
}
