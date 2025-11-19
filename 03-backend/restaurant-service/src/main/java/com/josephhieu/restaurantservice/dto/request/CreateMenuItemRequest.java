package com.josephhieu.restaurantservice.dto.request;

import lombok.Data;

@Data
public class CreateMenuItemRequest {
    private String restaurantId;
    private String name;
    private long price;
    private String description;
}