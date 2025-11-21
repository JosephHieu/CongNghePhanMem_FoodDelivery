package com.josephhieu.restaurantservice.controller;


import com.josephhieu.restaurantservice.dto.request.CreateRestaurantRequest;
import com.josephhieu.restaurantservice.entity.Restaurant;
import com.josephhieu.restaurantservice.service.ImageService;
import com.josephhieu.restaurantservice.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {


    private final RestaurantService service;
    private final ImageService imageService;


    @PostMapping
    public ResponseEntity<Restaurant> create(@RequestBody CreateRestaurantRequest req) {
        return ResponseEntity.ok(service.create(req));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable String id) {
        Restaurant r = service.get(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }


    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        try {
            String url = imageService.upload(file);
            return ResponseEntity.ok(service.uploadImage(id, url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<?> getInternal(@PathVariable String id) {
        Restaurant r = service.get(id);
        if (r == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(r);
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody CreateRestaurantRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(service.updateStatus(id, body.get("status")));
    }
}