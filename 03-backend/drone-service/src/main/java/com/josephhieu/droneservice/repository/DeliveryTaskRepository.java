package com.josephhieu.droneservice.repository;

import com.josephhieu.droneservice.entity.DeliveryTask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTaskRepository extends MongoRepository<DeliveryTask, String> {
}
