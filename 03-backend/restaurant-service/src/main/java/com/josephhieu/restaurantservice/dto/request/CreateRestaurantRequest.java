package com.josephhieu.restaurantservice.dto.request;


import lombok.Data;


@Data
public class CreateRestaurantRequest {
    private String ownerId;
    private String name;
    private String address;
    private double lat;
    private double lng;
}