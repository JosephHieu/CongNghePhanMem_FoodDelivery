package com.josephhieu.droneservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "drones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drone {

    @Id
    private String id;

    private String name;

    private double lat;
    private double lng;

    private Boolean available;

    @Builder.Default
    private String status = "IDLE"; // IDLE, DELIVERING, RETURNING, OFFLINE

    private String currentTaskId; // 🔥 Drone đang làm task nào

    // Optional fields for real flight simulation
    @Builder.Default
    private double speed = 10.0; // m/s

    @Builder.Default
    private double battery = 100.0; // percent

    private Double heading; // hướng bay (0–360 độ)

    // timestamps
    private Date createdAt;
    private Date updatedAt;
}
