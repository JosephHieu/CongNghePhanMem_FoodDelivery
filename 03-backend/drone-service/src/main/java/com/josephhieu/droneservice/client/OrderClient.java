package com.josephhieu.droneservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

    @PostMapping("/api/orders/{id}/delivered")
    void markDelivered(@PathVariable("id") String id);
}
