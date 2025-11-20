package com.josephhieu.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentClient {

    // create payment and return payment url info
    @PostMapping("/api/payments/create")
    Map<String, Object> createPayment(@RequestBody Map<String, Object> payload);
}
