package com.josephhieu.droneservice.dto;

import lombok.Data;

@Data
public class AssignDroneRequest {
    private String orderId;
    private LocationDto restaurant;
    private LocationDto customer;
}

