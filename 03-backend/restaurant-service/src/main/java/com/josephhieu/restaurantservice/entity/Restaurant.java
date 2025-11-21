package com.josephhieu.restaurantservice.entity;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;


@Document(collection = "restaurants")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Restaurant {


    @Id
    @JsonProperty("_id")
    private String id;
    private String ownerId;
    private String name;
    private String address;
    private double lat;
    private double lng;
    private String imageUrl; // Cloudinary
    private List<String> menuItemIds;

    private Integer status;
}