package com.josephhieu.restaurantservice.service;


import com.josephhieu.restaurantservice.dto.request.CreateRestaurantRequest;
import com.josephhieu.restaurantservice.entity.Restaurant;
import com.josephhieu.restaurantservice.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}