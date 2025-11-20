package com.josephhieu.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {
    @PostMapping("/api/orders/{orderId}/payment-callback")
    void markOrderPaid(@PathVariable String orderId, @RequestBody Map<String, String> payload);

    @PutMapping("/api/orders/{id}/status")
    void updateStatus(@PathVariable("id") String id, @RequestBody Map<String, String> body);
}
