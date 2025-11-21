package com.josephhieu.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    @Id
    @JsonProperty("_id")
    private String id;

    private String userId;
    private String restaurantId;

    private List<OrderItem> items;

    private Long totalPrice;

    private Status status; // <-- sử dụng enum riêng

    private String paymentUrl;
    private String paymentId;

    private Date createdAt;
    private Date updatedAt;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        private String menuItemId;
        private int quantity;
        private long price;
    }
}
