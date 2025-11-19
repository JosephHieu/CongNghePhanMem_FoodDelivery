package com.josephhieu.restaurantservice.controller;


import com.josephhieu.restaurantservice.dto.request.CreateMenuItemRequest;
import com.josephhieu.restaurantservice.entity.MenuItem;
import com.josephhieu.restaurantservice.service.ImageService;
import com.josephhieu.restaurantservice.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;


@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {


    private final MenuItemService service;
    private final ImageService imageService;


    @PostMapping
    public ResponseEntity<MenuItem> create(@RequestBody CreateMenuItemRequest req) {
        return ResponseEntity.ok(service.create(req));
    }


    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<MenuItem>> getByRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(service.getByRestaurant(id));
    }


    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> upload(@PathVariable String id, @RequestParam("file") MultipartFile f) {
        try {
            String url = imageService.upload(f);
            return ResponseEntity.ok(service.uploadImage(id, url));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}