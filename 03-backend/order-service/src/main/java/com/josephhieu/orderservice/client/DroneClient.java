package com.josephhieu.orderservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "drone-service")
public interface DroneClient {

    @PostMapping("/api/drones/assign")
    Map<String, Object> assignDrone(@RequestBody Map<String, Object> payload);
}
