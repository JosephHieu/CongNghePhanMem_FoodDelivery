package com.josephhieu.userservice.controller;

import com.josephhieu.userservice.dto.request.LoginRequest;
import com.josephhieu.userservice.dto.request.RegisterRequest;
import com.josephhieu.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        return ResponseEntity.ok(
                java.util.Map.of("token", authService.login(req))
        );
    }
}
