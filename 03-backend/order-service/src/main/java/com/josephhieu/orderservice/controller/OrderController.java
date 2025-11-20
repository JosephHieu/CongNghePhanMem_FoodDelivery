package com.josephhieu.orderservice.controller;

import com.josephhieu.orderservice.dto.request.CreateOrderRequest;
import com.josephhieu.orderservice.dto.request.UpdateStatusRequest;
import com.josephhieu.orderservice.entity.Order;
import com.josephhieu.orderservice.entity.Status;
import com.josephhieu.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest req) {
        return ResponseEntity.ok(orderService.create(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable String id) {
        Order o = orderService.getById(id);
        if (o == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(o);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(orderService.getByUser(userId));
    }

    // Endpoint used by payment-service callback to mark order paid
    @PostMapping("/{id}/payment-callback")
    public ResponseEntity<?> paymentCallback(@PathVariable String id, @RequestBody Map<String, String> payload) {
        // payload can contain: paymentId, statusCode...
        String paymentId = payload.get("paymentId");
        orderService.markPaid(id, paymentId);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable String id,
            @RequestBody Map<String, String> body
    ) {
        try {
            String statusStr = body.get("status");
            Status status = Status.valueOf(statusStr); // convert string -> enum

            Order updated = orderService.updateStatus(id, status);
            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + body.get("status"));
        }
    }
}
