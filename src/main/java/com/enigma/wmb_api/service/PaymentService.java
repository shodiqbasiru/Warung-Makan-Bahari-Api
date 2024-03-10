package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.entity.Payment;

public interface PaymentService {
    Payment createPayment(Bill bill);

}
