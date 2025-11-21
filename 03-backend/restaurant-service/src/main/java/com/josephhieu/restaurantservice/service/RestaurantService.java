package com.josephhieu.restaurantservice.service;


import com.josephhieu.restaurantservice.dto.request.CreateRestaurantRequest;
import com.josephhieu.restaurantservice.entity.Restaurant;
import com.josephhieu.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {


    private final RestaurantRepository restaurantRepository;

    public Restaurant create(CreateRestaurantRequest req) {
        Restaurant r = Restaurant.builder()
                .ownerId(req.getOwnerId())
                .name(req.getName())
                .address(req.getAddress())
                .lat(req.getLat())
                .lng(req.getLng())
                .build();
        return restaurantRepository.save(r);
    }

    public Restaurant get(String id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Restaurant uploadImage(String restaurantId, String imageUrl) {
        Restaurant r = restaurantRepository.findById(restaurantId).orElseThrow();
        r.setImageUrl(imageUrl);
        return restaurantRepository.save(r);
    }

    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    public Restaurant update(String id, CreateRestaurantRequest req) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        r.setName(req.getName());
        r.setAddress(req.getAddress());
        r.setLat(req.getLat());
        r.setLng(req.getLng());
        r.setOwnerId(req.getOwnerId());

        return restaurantRepository.save(r);
    }

    public Restaurant updateStatus(String id, int status) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        r.setLng(r.getLng()); // giữ nguyên
        r.setLat(r.getLat());
        r.setOwnerId(r.getOwnerId());
        r.setName(r.getName());

        // Add status field manually since Restaurant doesn't have it yet
        // YOU MUST ADD THIS FIELD
        // private Integer status;
        // in Restaurant.java

        r.setStatus(status);
        return restaurantRepository.save(r);
    }

    public Restaurant createWithImage(CreateRestaurantRequest req, String imageUrl) {
        Restaurant r = Restaurant.builder()
                .ownerId(req.getOwnerId())
                .name(req.getName())
                .address(req.getAddress())
                .lat(req.getLat())
                .lng(req.getLng())
                .imageUrl(imageUrl)
                .build();

        return restaurantRepository.save(r);
    }
}