package com.josephhieu.droneservice.dto;

import lombok.Data;

@Data
public class AssignDroneRequest {
    private String orderId;

    private LocationDto restaurant;
    private String restaurantName;

    private LocationDto customer;
    private String customerName;
    private String customerPhone;
}

