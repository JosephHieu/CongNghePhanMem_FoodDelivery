package com.josephhieu.restaurantservice.service;

import com.josephhieu.restaurantservice.dto.request.CreateMenuItemRequest;
import com.josephhieu.restaurantservice.entity.MenuItem;
import com.josephhieu.restaurantservice.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {


    private final MenuItemRepository repo;


    public MenuItem create(CreateMenuItemRequest req) {
        MenuItem m = MenuItem.builder()
                .restaurantId(req.getRestaurantId())
                .name(req.getName())
                .price(req.getPrice())
                .description(req.getDescription())
                .build();
        return repo.save(m);
    }


    public List<MenuItem> getByRestaurant(String restaurantId) {
        return repo.findByRestaurantId(restaurantId);
    }

    public MenuItem uploadImage(String menuItemId, String imageUrl) {
        MenuItem m = repo.findById(menuItemId).orElseThrow();
        m.setImageUrl(imageUrl);
        return repo.save(m);
    }
}