package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.dto.HotelDto;
import com.hotelreservation.template.repository.HotelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelDto> getAll() {
        return hotelRepository.findAll().stream().map(HotelService::toDto).toList();
    }

    public HotelDto getById(Long id) {
        return toDto(findEntity(id));
    }

    public HotelDto create(HotelDto dto) {
        Hotel hotel = new Hotel(dto.name(), dto.address(), dto.city(), dto.starRating());
        return toDto(hotelRepository.save(hotel));
    }

    public void delete(Long id) {
        hotelRepository.delete(findEntity(id));
    }

    private Hotel findEntity(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + id));
    }

    private static HotelDto toDto(Hotel hotel) {
        return new HotelDto(hotel.getId(), hotel.getName(), hotel.getAddress(), hotel.getCity(), hotel.getStarRating());
    }
}
