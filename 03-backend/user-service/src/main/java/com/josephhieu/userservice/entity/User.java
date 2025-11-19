package com.josephhieu.userservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String fullName;

    private String email;

    private String phone;

    private String passwordHash;

    private Role role;       // ADMIN, CUSTOMER, RESTAURANT_OWNER

    private Integer status;  // 1 = active, 0 = banned

    private List<Address> addresses;

    private Date createdAt;

    private Date updatedAt;

    // -------------------------
    // Nested classes
    // -------------------------

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String name;
        private double lat;
        private double lng;
    }

    public enum Role {
        ADMIN,
        CUSTOMER,
        RESTAURANT_OWNER
    }
}
