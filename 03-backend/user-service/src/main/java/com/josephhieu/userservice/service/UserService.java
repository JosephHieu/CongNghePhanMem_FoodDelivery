package com.josephhieu.userservice.service;

import com.josephhieu.userservice.entity.User;
import com.josephhieu.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
}
