package com.novacart.store.service;

import com.novacart.store.dto.CustomerProfileResponse;
import java.util.List;

public interface CustomerService {

    List<CustomerProfileResponse> findCustomers();
}
