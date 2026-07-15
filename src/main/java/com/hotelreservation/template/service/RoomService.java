package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.RoomDto;
import com.hotelreservation.template.mapper.RoomMapper;
import com.hotelreservation.template.repository.HotelRepository;
import com.hotelreservation.template.repository.RoomRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

  private final RoomRepository roomRepository;
  private final HotelRepository hotelRepository;
  private final RoomMapper roomMapper;

  public RoomService(
      RoomRepository roomRepository, HotelRepository hotelRepository, RoomMapper roomMapper) {
    this.roomRepository = roomRepository;
    this.hotelRepository = hotelRepository;
    this.roomMapper = roomMapper;
  }

  public List<RoomDto> getAll() {
    return roomRepository.findAll().stream().map(roomMapper::toDto).toList();
  }

  public List<RoomDto> getByHotel(Long hotelId) {
    return roomRepository.findByHotelId(hotelId).stream().map(roomMapper::toDto).toList();
  }

  public RoomDto getById(Long id) {
    return roomMapper.toDto(findEntity(id));
  }

  public RoomDto create(RoomDto dto) {
    Hotel hotel =
        hotelRepository
            .findById(dto.hotelId())
            .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + dto.hotelId()));
    Room room =
        new Room(hotel, dto.roomNumber(), dto.roomType(), dto.pricePerNight(), dto.maxOccupancy());
    return roomMapper.toDto(roomRepository.save(room));
  }

  public void delete(Long id) {
    roomRepository.delete(findEntity(id));
  }

  private Room findEntity(Long id) {
    return roomRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
  }
}
