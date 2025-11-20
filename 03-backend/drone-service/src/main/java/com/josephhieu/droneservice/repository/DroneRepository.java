package com.josephhieu.droneservice.repository;

import com.josephhieu.droneservice.entity.Drone;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DroneRepository extends MongoRepository<Drone, String> {

    List<Drone> findByAvailable(boolean available);

    Optional<Drone> findFirstByAvailable(boolean available);
}
