package com.josephhieu.orderservice.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String userId;
    private String restaurantId;
    private List<Item> items;

    @Data
    public static class Item {
        private String menuItemId;
        private int quantity;
        private long price;
    }
}
