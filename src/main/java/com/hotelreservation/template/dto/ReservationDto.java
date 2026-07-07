package com.hotelreservation.template.dto;

import com.hotelreservation.template.domain.ReservationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservationDto(
        Long id,
        @NotNull(message = "roomId is required") Long roomId,
        @NotBlank(message = "guestName is required") String guestName,
        @NotBlank @Email(message = "guestEmail must be a valid email") String guestEmail,
        @NotNull(message = "checkInDate is required") LocalDate checkInDate,
        @NotNull(message = "checkOutDate is required") LocalDate checkOutDate,
        ReservationStatus status
) {
}
