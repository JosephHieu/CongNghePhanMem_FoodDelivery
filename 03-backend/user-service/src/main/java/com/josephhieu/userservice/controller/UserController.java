package com.josephhieu.userservice.controller;

import com.josephhieu.userservice.entity.User;
import com.josephhieu.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getProfile(Authentication auth) {
        String id = (String) auth.getPrincipal();
        User u = userService.getUser(id);
        return ResponseEntity.ok(u);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication auth, @RequestBody User req) {
        String id = (String) auth.getPrincipal();
        return ResponseEntity.ok(userService.updateUser(id, req));
    }

    @GetMapping("/internal/{id}")
    public ResponseEntity<User> getUserInternal(@PathVariable String id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}
