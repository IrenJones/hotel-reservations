package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.dto.HotelDto;
import com.hotelreservation.template.mapper.HotelMapper;
import com.hotelreservation.template.repository.HotelRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

  private final HotelRepository hotelRepository;
  private final HotelMapper hotelMapper;

  public HotelService(HotelRepository hotelRepository, HotelMapper hotelMapper) {
    this.hotelRepository = hotelRepository;
    this.hotelMapper = hotelMapper;
  }

  public List<HotelDto> getAll() {
    return hotelRepository.findAll().stream().map(hotelMapper::toDto).toList();
  }

  public HotelDto getById(Long id) {
    return hotelMapper.toDto(findEntity(id));
  }

  public HotelDto create(HotelDto dto) {
    Hotel hotel = new Hotel(dto.name(), dto.address(), dto.city(), dto.starRating());
    return hotelMapper.toDto(hotelRepository.save(hotel));
  }

  public void delete(Long id) {
    hotelRepository.delete(findEntity(id));
  }

  private Hotel findEntity(Long id) {
    return hotelRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + id));
  }
}
