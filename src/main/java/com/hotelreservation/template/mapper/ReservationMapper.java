package com.hotelreservation.template.mapper;

import com.hotelreservation.template.domain.Reservation;
import com.hotelreservation.template.dto.ReservationDto;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

  public ReservationDto toDto(Reservation reservation) {
    return new ReservationDto(
        reservation.getId(),
        reservation.getRoom().getId(),
        reservation.getGuestName(),
        reservation.getGuestEmail(),
        reservation.getCheckInDate(),
        reservation.getCheckOutDate(),
        reservation.getStatus(),
        reservation.getPromoCode(),
        reservation.getSubtotalPrice(),
        reservation.getDiscountAmount(),
        reservation.getTotalPrice());
  }
}
