package com.josephhieu.droneservice.service;

import com.josephhieu.droneservice.client.OrderClient;
import com.josephhieu.droneservice.dto.AssignDroneRequest;
import com.josephhieu.droneservice.entity.DeliveryTask;
import com.josephhieu.droneservice.entity.Drone;
import com.josephhieu.droneservice.repository.DeliveryTaskRepository;
import com.josephhieu.droneservice.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneService {

    private final DroneRepository droneRepo;
    private final DeliveryTaskRepository taskRepo;
    private final OrderClient orderClient;

    /**
     * Assign available drone
     */
    public DeliveryTask assignDrone(AssignDroneRequest req) {

        // 1. Find available drone
        Optional<Drone> optionalDrone = droneRepo.findFirstByAvailable(true);

        if (optionalDrone.isEmpty()) {
            throw new RuntimeException("No available drone");
        }

        Drone drone = optionalDrone.get();

        // Mark drone busy
        drone.setAvailable(false);
        droneRepo.save(drone);

        // 2. Create delivery task
        DeliveryTask task = DeliveryTask.builder()
                .orderId(req.getOrderId())
                .droneId(drone.getId())

                .restaurantLat(req.getRestaurant().getLat())
                .restaurantLng(req.getRestaurant().getLng())

                .customerLat(req.getCustomer().getLat())
                .customerLng(req.getCustomer().getLng())

                .status("ASSIGNED")
                .createdAt(new Date())
                .updatedAt(new Date())
                .assignedAt(new Date())
                .build();


        return taskRepo.save(task);
    }

    /**
     * Complete delivery
     */
    public void completeDelivery(String taskId) {

        DeliveryTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Delivery Task not found"));

        task.setStatus("COMPLETED");
        taskRepo.save(task);

        // Free up drone
        Drone drone = droneRepo.findById(task.getDroneId())
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        drone.setAvailable(true);
        droneRepo.save(drone);

        // Notify Order Service
        orderClient.markDelivered(task.getOrderId());
    }

    public Drone createDrone(String name) {
        Drone drone = Drone.builder()
                .name(name)
                .available(true)
                .build();
        return droneRepo.save(drone);
    }

    public Drone updateDrone(String id, String name, Boolean available) {
        Drone drone = droneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        if (name != null) drone.setName(name);
        if (available != null) drone.setAvailable(available);

        return droneRepo.save(drone);
    }

    public void deleteDrone(String id) {
        if (!droneRepo.existsById(id)) {
            throw new RuntimeException("Drone not found");
        }
        droneRepo.deleteById(id);
    }

    public Drone getDrone(String id) {
        return droneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone not found"));
    }
}
