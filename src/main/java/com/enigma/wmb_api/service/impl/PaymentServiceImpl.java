package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.PaymentDetailRequest;
import com.enigma.wmb_api.dto.request.PaymentItemDetailRequest;
import com.enigma.wmb_api.dto.request.PaymentRequest;
import com.enigma.wmb_api.entity.Bill;
import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.repository.PaymentRepository;
import com.enigma.wmb_api.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestClient restClient;
    private final String SECRET_KEY;
    private final String SNAP_BASE_URL;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            RestClient restClient,
            @Value("${midtrans.api.key}") String secretKey,
            @Value("${midtrans.api.snap-url}") String snapBaseUrl
    ) {
        this.paymentRepository = paymentRepository;
        this.restClient = restClient;
        SECRET_KEY = secretKey;
        SNAP_BASE_URL = snapBaseUrl;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Payment createPayment(Bill bill) {
        long amount = bill.getBillDetails().stream()
                .mapToInt(value -> ( value.getQty() * value.getPrice()))
                .reduce(0, Integer::sum);

        PaymentDetailRequest paymentDetailRequest = PaymentDetailRequest.builder()
                .orderId(bill.getId())
                .amount(amount)
                .build();

        List<PaymentItemDetailRequest> itemDetailRequests = bill.getBillDetails().stream()
                .map(billDetail -> PaymentItemDetailRequest.builder()
                        .name(billDetail.getMenu().getMenuName())
                        .price(billDetail.getPrice())
                        .quantity(billDetail.getQty())
                        .build())
                .toList();

        List<String> paymentMethods = List.of("shopeepay", "gopay");

        PaymentRequest request = PaymentRequest.builder()
                .paymentDetail(paymentDetailRequest)
                .paymentItemDetails(itemDetailRequests)
                .paymentMethod(paymentMethods)
                .build();

        ResponseEntity<Map<String, String>> response = restClient.post()
                .uri(SNAP_BASE_URL)
                .body(request)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
                .retrieve().toEntity(new ParameterizedTypeReference<>() {
                });
        Map<String, String> body = response.getBody();

        Payment payment = Payment.builder()
                .token(body.get("token"))
                .redirectUrl(body.get("redirect_url"))
                .transactionStatus("ordered")
                .build();
        paymentRepository.saveAndFlush(payment);
        return payment;
    }

}
