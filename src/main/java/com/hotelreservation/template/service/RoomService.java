package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.RoomDto;
import com.hotelreservation.template.repository.HotelRepository;
import com.hotelreservation.template.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
    }

    public List<RoomDto> getAll() {
        return roomRepository.findAll().stream().map(RoomService::toDto).toList();
    }

    public List<RoomDto> getByHotel(Long hotelId) {
        return roomRepository.findByHotelId(hotelId).stream().map(RoomService::toDto).toList();
    }

    public RoomDto getById(Long id) {
        return toDto(findEntity(id));
    }

    public RoomDto create(RoomDto dto) {
        Hotel hotel = hotelRepository.findById(dto.hotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + dto.hotelId()));
        Room room = new Room(hotel, dto.roomNumber(), dto.roomType(), dto.pricePerNight(), dto.maxOccupancy());
        return toDto(roomRepository.save(room));
    }

    public void delete(Long id) {
        roomRepository.delete(findEntity(id));
    }

    private Room findEntity(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
    }

    private static RoomDto toDto(Room room) {
        return new RoomDto(room.getId(), room.getHotel().getId(), room.getRoomNumber(), room.getRoomType(),
                room.getPricePerNight(), room.getMaxOccupancy());
    }
}
