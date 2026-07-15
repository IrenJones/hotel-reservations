package com.hotelreservation.template.mapper;

import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.RoomDto;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

  public RoomDto toDto(Room room) {
    return new RoomDto(
        room.getId(),
        room.getHotel().getId(),
        room.getRoomNumber(),
        room.getRoomType(),
        room.getPricePerNight(),
        room.getMaxOccupancy());
  }
}
