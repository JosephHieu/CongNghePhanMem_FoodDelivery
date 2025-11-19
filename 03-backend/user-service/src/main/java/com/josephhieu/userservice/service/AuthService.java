package com.josephhieu.userservice.service;

import com.josephhieu.userservice.dto.request.LoginRequest;
import com.josephhieu.userservice.dto.request.RegisterRequest;
import com.josephhieu.userservice.entity.User;
import com.josephhieu.userservice.repository.UserRepository;
import com.josephhieu.userservice.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public User register(RegisterRequest req) {

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email has already been used");
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(encoder.encode(req.getPassword()))
                .role(User.Role.valueOf(req.getRole()))
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return userRepository.save(user);
    }

    public String login(LoginRequest req) {

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Incorrect password");
        }

        return jwtUtil.generateToken(
                user.getId(),
                user.getRole().name()
        );
    }
}
