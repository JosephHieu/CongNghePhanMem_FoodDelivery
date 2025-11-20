package com.josephhieu.droneservice.service;

import com.josephhieu.droneservice.client.OrderClient;
import com.josephhieu.droneservice.dto.AssignDroneRequest;
import com.josephhieu.droneservice.dto.DroneLocationMessage;
import com.josephhieu.droneservice.entity.DeliveryTask;
import com.josephhieu.droneservice.entity.Drone;
import com.josephhieu.droneservice.repository.DeliveryTaskRepository;
import com.josephhieu.droneservice.repository.DroneRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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

    @Autowired
    private SimpMessagingTemplate messaging;

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
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus("COMPLETED");
        task.setCompletedAt(new Date());
        taskRepo.save(task);

        // Free drone
        Drone drone = droneRepo.findById(task.getDroneId()).get();
        drone.setAvailable(true);
        drone.setStatus("IDLE");
        droneRepo.save(drone);

        // Notify order-service
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

    public Drone updateLocation(String id, double lat, double lng) {

        Drone drone = droneRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Drone not found"));

        drone.setLat(lat);
        drone.setLng(lng);
        drone.setStatus("DELIVERING");
        drone.setAvailable(false);
        drone.setUpdatedAt(new Date());

        Drone saved = droneRepo.save(drone);

        // 🔥 Gửi realtime WebSocket update
        messaging.convertAndSend(
                "/topic/drone-location",
                new DroneLocationMessage(
                        drone.getId(),
                        drone.getLat(),
                        drone.getLng(),
                        drone.getStatus()
                )
        );

        return saved;
    }

    public void simulateFlight(String id, double targetLat, double targetLng) {

        new Thread(() -> {
            Drone drone = droneRepo.findById(id).orElseThrow();

            double step = 20.0;

            double latStep = (targetLat - drone.getLat()) / step;
            double lngStep = (targetLng - drone.getLng()) / step;

            for (int i = 0; i < step; i++) {
                drone.setLat(drone.getLat() + latStep);
                drone.setLng(drone.getLng() + lngStep);
                drone.setStatus("DELIVERING");
                droneRepo.save(drone);

                // 🔥 Gửi realtime qua WebSocket
                messaging.convertAndSend(
                        "/topic/drone-location",
                        new DroneLocationMessage(
                                drone.getId(),
                                drone.getLat(),
                                drone.getLng(),
                                drone.getStatus()
                        )
                );

                try { Thread.sleep(500); } catch (Exception ignored) {}
            }

            drone.setStatus("IDLE");
            drone.setAvailable(true);
            droneRepo.save(drone);

            messaging.convertAndSend(
                    "/topic/drone-location",
                    new DroneLocationMessage(drone.getId(), drone.getLat(), drone.getLng(), drone.getStatus())
            );

        }).start();
    }
}
