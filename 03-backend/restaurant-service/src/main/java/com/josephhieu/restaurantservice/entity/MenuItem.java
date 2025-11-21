package com.josephhieu.restaurantservice.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "menu_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItem {


    @Id
    @JsonProperty("_id")
    private String id;
    private String restaurantId;
    private String name;
    private long price;
    private String imageUrl; // Cloudinary
    private String description;
}