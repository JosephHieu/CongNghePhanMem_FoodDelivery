package com.josephhieu.droneservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "deliveries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryTask {

    @Id
    private String id;

    private String orderId;
    private String droneId;

    private double restaurantLat;
    private double restaurantLng;

    private double customerLat;
    private double customerLng;

    private String status;
    // ASSIGNED → PICKING → DELIVERING → COMPLETED

    private Date createdAt;
    private Date updatedAt;


    private Date assignedAt;
    private Date pickedAt;
    private Date deliveringAt;
    private Date completedAt;
}

