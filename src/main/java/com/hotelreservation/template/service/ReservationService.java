package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.Reservation;
import com.hotelreservation.template.domain.ReservationStatus;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.repository.ReservationRepository;
import com.hotelreservation.template.repository.RoomRepository;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final RoomRepository roomRepository;

  public ReservationService(
      ReservationRepository reservationRepository, RoomRepository roomRepository) {
    this.reservationRepository = reservationRepository;
    this.roomRepository = roomRepository;
  }

  public List<ReservationDto> getAll() {
    return reservationRepository.findAll().stream().map(ReservationService::toDto).toList();
  }

  public List<ReservationDto> getByRoom(Long roomId) {
    return reservationRepository.findByRoomId(roomId).stream()
        .map(ReservationService::toDto)
        .toList();
  }

  public ReservationDto getById(Long id) {
    return toDto(findEntity(id));
  }

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
    BigDecimal totalPrice = room.getPricePerNight().multiply(BigDecimal.valueOf(nights));
    Reservation reservation =
        new Reservation(
            room,
            dto.guestName(),
            dto.guestEmail(),
            dto.checkInDate(),
            dto.checkOutDate(),
            status,
            totalPrice);
    return toDto(reservationRepository.save(reservation));
  }

  public void delete(Long id) {
    reservationRepository.delete(findEntity(id));
  }

  private Reservation findEntity(Long id) {
    return reservationRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Reservation not found: " + id));
  }

  private static ReservationDto toDto(Reservation r) {
    return new ReservationDto(
        r.getId(),
        r.getRoom().getId(),
        r.getGuestName(),
        r.getGuestEmail(),
        r.getCheckInDate(),
        r.getCheckOutDate(),
        r.getStatus(),
        r.getTotalPrice());
  }
}
