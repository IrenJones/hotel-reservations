package com.hotelreservation.template.dto;

import jakarta.validation.constraints.NotBlank;

public record HotelDto(
    Long id,
    @NotBlank(message = "name is required") String name,
    String address,
    @NotBlank(message = "city is required") String city,
    Integer starRating) {}
