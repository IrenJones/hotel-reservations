package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.Reservation;
import com.hotelreservation.template.domain.ReservationStatus;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.mapper.ReservationMapper;
import com.hotelreservation.template.repository.ReservationRepository;
import com.hotelreservation.template.repository.RoomRepository;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;
  private final ReservationPricingService reservationPricingService;
  private final ReservationMapper reservationMapper;

  public ReservationService(
      ReservationRepository reservationRepository,
      RoomRepository roomRepository,
      ReservationPricingService reservationPricingService,
      ReservationMapper reservationMapper) {
    this.reservationRepository = reservationRepository;
    this.roomRepository = roomRepository;
    this.reservationPricingService = reservationPricingService;
    this.reservationMapper = reservationMapper;
  }

  public List<ReservationDto> getAll() {
    return reservationRepository.findAll().stream().map(reservationMapper::toDto).toList();
  }

  public List<ReservationDto> getByRoom(Long roomId) {
    return reservationRepository.findByRoomId(roomId).stream()
        .map(reservationMapper::toDto)
        .toList();
  }

  public ReservationDto getById(Long id) {
    return reservationMapper.toDto(findEntity(id));
  }

  @Transactional
  public ReservationDto create(ReservationDto dto) {
    if (!dto.checkOutDate().isAfter(dto.checkInDate())) {
      throw new IllegalArgumentException("checkOutDate must be after checkInDate");
    }
    Room room =
        roomRepository
            .findById(dto.roomId())
            .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + dto.roomId()));
    ReservationStatus status = dto.status() != null ? dto.status() : ReservationStatus.PENDING;
    long nights = ChronoUnit.DAYS.between(dto.checkInDate(), dto.checkOutDate());
    ReservationPricingService.ReservationPrice price =
        reservationPricingService.calculate(room.getPricePerNight(), nights, dto.promoCode());
    Reservation reservation =
        new Reservation(
            room,
            dto.guestName(),
            dto.guestEmail(),
            dto.checkInDate(),
            dto.checkOutDate(),
            status,
            price.subtotalPrice(),
            price.discountAmount(),
            price.totalPrice(),
            price.promoCode());
    return reservationMapper.toDto(reservationRepository.save(reservation));
  }

  public void delete(Long id) {
    reservationRepository.delete(findEntity(id));
  }

  private Reservation findEntity(Long id) {
    return reservationRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + id));
  }
}
