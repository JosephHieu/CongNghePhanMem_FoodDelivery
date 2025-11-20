package com.josephhieu.droneservice.controller;

import com.josephhieu.droneservice.dto.AssignDroneRequest;
import com.josephhieu.droneservice.entity.DeliveryTask;
import com.josephhieu.droneservice.entity.Drone;
import com.josephhieu.droneservice.repository.DeliveryTaskRepository;
import com.josephhieu.droneservice.repository.DroneRepository;
import com.josephhieu.droneservice.service.DroneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;
    private final DroneRepository droneRepo;
    private final DeliveryTaskRepository taskRepo;

    /**
     * Assign a drone to an order
     */
    @PostMapping("/assign")
    public ResponseEntity<?> assignDrone(@RequestBody AssignDroneRequest request) {
        DeliveryTask task = droneService.assignDrone(request);
        return ResponseEntity.ok(task);
    }

    /**
     * Mark delivery task completed
     */
    @PostMapping("/complete/{taskId}")
    public ResponseEntity<?> completeDelivery(@PathVariable String taskId) {
        droneService.completeDelivery(taskId);
        return ResponseEntity.ok("Delivery completed");
    }

    /**
     * Get all drones
     */
    @GetMapping
    public ResponseEntity<List<Drone>> getAllDrones() {
        return ResponseEntity.ok(droneRepo.findAll());
    }

    /**
     * Get all delivery tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<DeliveryTask>> getAllTasks() {
        return ResponseEntity.ok(taskRepo.findAll());
    }

    /**
     * Create drone
     */
    @PostMapping
    public ResponseEntity<Drone> createDrone(@RequestBody Drone request) {
        Drone drone = droneService.createDrone(request.getName());
        return ResponseEntity.ok(drone);
    }

    /**
     * Update drone info
     */
    @PutMapping("/{id}")
    public ResponseEntity<Drone> updateDrone(
            @PathVariable String id,
            @RequestBody Drone request
    ) {
        Drone updated = droneService.updateDrone(id, request.getName(), request.getAvailable());
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete drone
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrone(@PathVariable String id) {
        droneService.deleteDrone(id);
        return ResponseEntity.ok("Drone deleted");
    }

    /**
     * Get single drone
     */
    @GetMapping("/{id}")
    public ResponseEntity<Drone> getDrone(@PathVariable String id) {
        return ResponseEntity.ok(droneService.getDrone(id));
    }
}
