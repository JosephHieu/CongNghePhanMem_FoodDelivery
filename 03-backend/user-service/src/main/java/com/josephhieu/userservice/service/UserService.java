package com.josephhieu.userservice.service;

import com.josephhieu.userservice.dto.request.RegisterRequest;
import com.josephhieu.userservice.entity.User;
import com.josephhieu.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUser(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(String id, User update) {
        User u = userRepository.findById(id).orElse(null);
        if (u == null) return null;

        if (update.getFullName() != null) u.setFullName(update.getFullName());
        if (update.getPhone() != null) u.setPhone(update.getPhone());
        if (update.getAddresses() != null) u.setAddresses(update.getAddresses());

        u.setUpdatedAt(new Date());

        return userRepository.save(u);
    }

    public User getById(String id) {
        return  userRepository.findById(id).orElseThrow(null);
    }

    public User updateStatus(String id, int status) {
        User u = userRepository.findById(id).orElseThrow();
        u.setStatus(status);
        u.setUpdatedAt(new Date());
        return userRepository.save(u);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    // ============================
    // CREATE USER (ADMIN)
    // ============================
    public User createUser(RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .phone(req.getPhone())
                .passwordHash(passwordEncoder.encode(req.getPassword()))
                .role(User.Role.valueOf(req.getRole()))
                .status(1)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        return userRepository.save(user);
    }


    // ============================
    // UPDATE USER (ADMIN)
    // ============================
    public User updateUserByAdmin(String id, Map<String, Object> body) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (body.containsKey("fullName"))
            user.setFullName(body.get("fullName").toString());

        if (body.containsKey("email"))
            user.setEmail(body.get("email").toString());

        if (body.containsKey("phone"))
            user.setPhone(body.get("phone").toString());

        if (body.containsKey("role"))
            user.setRole(User.Role.valueOf(body.get("role").toString()));
        if (body.containsKey("status"))
            user.setStatus(Integer.parseInt(body.get("status").toString()));

        if (body.containsKey("password")) {
            user.setPasswordHash(passwordEncoder.encode(body.get("password").toString()));
        }

        user.setUpdatedAt(new Date());

        return userRepository.save(user);
    }


    // ============================
    // DELETE USER (ADMIN)
    // ============================
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }
}
