package com.josephhieu.droneservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "drones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Drone {
    @Id
    private String id;
    private double lat;
    private double lng;
    private boolean available;
    private String status; // IDLE, DELIVERING
}
