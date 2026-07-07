package com.hotelreservation.template.dto;

import com.hotelreservation.template.domain.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RoomDto(
        Long id,
        @NotNull(message = "hotelId is required") Long hotelId,
        @NotBlank(message = "roomNumber is required") String roomNumber,
        @NotNull(message = "roomType is required") RoomType roomType,
        @NotNull @Positive(message = "pricePerNight must be positive") BigDecimal pricePerNight,
        @NotNull @Positive(message = "maxOccupancy must be positive") Integer maxOccupancy
) {
}
