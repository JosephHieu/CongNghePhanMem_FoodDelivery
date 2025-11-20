package com.josephhieu.orderservice.service;

import com.josephhieu.orderservice.client.DroneClient;
import com.josephhieu.orderservice.client.PaymentClient;
import com.josephhieu.orderservice.client.RestaurantClient;
import com.josephhieu.orderservice.client.UserClient;
import com.josephhieu.orderservice.dto.request.CreateOrderRequest;
import com.josephhieu.orderservice.entity.Order;
import com.josephhieu.orderservice.entity.Status;
import com.josephhieu.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository repo;
    private final UserClient userClient;
    private final RestaurantClient restaurantClient;
    private final PaymentClient paymentClient;
    private final DroneClient droneClient;

    public Order create(CreateOrderRequest req) {
        // minimal validation: ensure user and restaurant exist (Feign call)
        userClient.getUser(req.getUserId()); // will throw if 404 from user-service
        restaurantClient.getRestaurant(req.getRestaurantId());

        long total = req.getItems().stream().mapToLong(i -> i.getPrice() * i.getQuantity()).sum();

        Order order = Order.builder()
                .userId(req.getUserId())
                .restaurantId(req.getRestaurantId())
                .items(req.getItems().stream().map(i -> new Order.OrderItem(i.getMenuItemId(), i.getQuantity(), i.getPrice())).toList())
                .totalPrice(total)
                .status(Status.PENDING_PAYMENT)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        Order saved = repo.save(order);

        // create payment via payment-service (asynchronous option)
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", saved.getId());
        payload.put("amount", saved.getTotalPrice());
        payload.put("description", "Payment for order " + saved.getId());
        // paymentClient returns e.g. { "paymentUrl": "...", "paymentId": "..." }
        try {
            Map<String, Object> resp = paymentClient.createPayment(payload);
            if (resp != null && resp.get("paymentUrl") != null) {
                saved.setPaymentUrl(String.valueOf(resp.get("paymentUrl")));
                if (resp.get("paymentId") != null) saved.setPaymentId(String.valueOf(resp.get("paymentId")));
                repo.save(saved);
            }
        } catch (Exception ex) {
            // payment service failed — keep order as PENDING_PAYMENT
        }

        return saved;
    }

    public Order getById(String id) {
        return repo.findById(id).orElse(null);
    }

    public java.util.List<Order> getByUser(String userId) {
        return repo.findByUserId(userId);
    }

    public Order updateStatus(String orderId, Status status) {

        Order order = repo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // cập nhật trạng thái
        order.setStatus(status);
        order.setUpdatedAt(new Date());

        Order updated = repo.save(order);

        // Nếu order đã sẵn sàng giao (nhà hàng chuẩn bị xong)
        if (status == Status.READY_FOR_DISPATCH) {

            Map<String, Object> payload = new HashMap<>();
            payload.put("orderId", orderId);
            payload.put("restaurantId", order.getRestaurantId());
            payload.put("deliveryAddress", Map.of("userId", order.getUserId()));

            try {
                droneClient.assignDrone(payload);
            } catch (Exception ex) {
                // TODO: Ghi log hoặc retry
                System.out.println("Drone-service unavailable: " + ex.getMessage());
            }
        }

        return updated;
    }


    // Called by payment-service callback to mark paid
    public Order markPaid(String orderId, String paymentId) {
        Order o = repo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        o.setStatus(Status.PAID);
        o.setPaymentId(paymentId);
        o.setUpdatedAt(new Date());
        return repo.save(o);
    }
}
