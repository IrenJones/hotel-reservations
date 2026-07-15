package com.hotelreservation.template.support;

import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.domain.ReservationStatus;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.domain.RoomType;
import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.repository.HotelRepository;
import com.hotelreservation.template.repository.RoomRepository;
import com.hotelreservation.template.service.ReservationService;
import jakarta.transaction.Transactional;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public abstract class ReservationIntegrationTestSupport {

  @Autowired protected ReservationService reservationService;

  @Autowired protected RoomRepository roomRepository;

  @Autowired protected Validator validator;

  @Autowired private HotelRepository hotelRepository;

  protected Room createRoom(String pricePerNight) {
    Hotel hotel =
        hotelRepository.save(new Hotel("Price Test Hotel", "1 Test Street", "Testville", 4));
    return roomRepository.save(
        new Room(hotel, "101", RoomType.DOUBLE, new BigDecimal(pricePerNight), 2));
  }

  protected ReservationDto reservationRequest(Long roomId, LocalDate checkIn, LocalDate checkOut) {
    return reservationRequest(roomId, checkIn, checkOut, null);
  }

  protected ReservationDto reservationRequest(
      Long roomId, LocalDate checkIn, LocalDate checkOut, BigDecimal totalPrice) {
    return new ReservationDto(
        null,
        roomId,
        "Test Guest",
        "guest@example.com",
        checkIn,
        checkOut,
        ReservationStatus.PENDING,
        totalPrice);
  }
}
