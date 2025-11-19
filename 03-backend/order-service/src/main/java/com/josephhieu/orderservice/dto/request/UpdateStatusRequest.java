package com.josephhieu.orderservice.dto.request;

import lombok.Data;

@Data
public class UpdateStatusRequest {
    private String status; // e.g., PAID, PREPARING, DELIVERING, COMPLETED
}
