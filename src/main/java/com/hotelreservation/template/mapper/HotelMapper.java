package com.hotelreservation.template.mapper;

import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.dto.HotelDto;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

  public HotelDto toDto(Hotel hotel) {
    return new HotelDto(
        hotel.getId(), hotel.getName(), hotel.getAddress(), hotel.getCity(), hotel.getStarRating());
  }
}
