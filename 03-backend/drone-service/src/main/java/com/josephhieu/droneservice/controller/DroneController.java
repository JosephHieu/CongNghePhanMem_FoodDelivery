package com.josephhieu.droneservice.controller;

import com.josephhieu.droneservice.dto.AssignDroneRequest;
import com.josephhieu.droneservice.entity.DeliveryTask;
import com.josephhieu.droneservice.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping("/assign")
    public ResponseEntity<?> assignDrone(@RequestBody AssignDroneRequest req) {
        DeliveryTask task = droneService.assignDrone(req);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{taskId}/complete")
    public ResponseEntity<?> completeDelivery(@PathVariable String taskId) {
        droneService.completeDelivery(taskId);
        return ResponseEntity.ok(
                java.util.Map.of("message", "Delivery completed", "taskId", taskId)
        );
    }
}
