package com.josephhieu.droneservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DroneLocationMessage {
    private String droneId;
    private double lat;
    private double lng;
    private String status;
}
