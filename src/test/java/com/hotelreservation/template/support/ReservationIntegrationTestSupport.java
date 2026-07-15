package com.hotelreservation.template.support;

import com.hotelreservation.template.domain.DiscountType;
import com.hotelreservation.template.domain.Hotel;
import com.hotelreservation.template.domain.ReservationStatus;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.domain.RoomType;
import com.hotelreservation.template.dto.PromoCodeDto;
import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.repository.HotelRepository;
import com.hotelreservation.template.repository.PromoCodeRepository;
import com.hotelreservation.template.repository.RoomRepository;
import com.hotelreservation.template.service.PromoCodeService;
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

  @Autowired protected PromoCodeService promoCodeService;

  @Autowired protected PromoCodeRepository promoCodeRepository;

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
      Long roomId, LocalDate checkIn, LocalDate checkOut, String promoCode) {
    return new ReservationDto(
        null,
        roomId,
        "Test Guest",
        "guest@example.com",
        checkIn,
        checkOut,
        ReservationStatus.PENDING,
        promoCode,
        null,
        null,
        null);
  }

  protected ReservationDto reservationRequestWithServerPrices(
      Long roomId, LocalDate checkIn, LocalDate checkOut, BigDecimal totalPrice) {
    return new ReservationDto(
        null,
        roomId,
        "Test Guest",
        "guest@example.com",
        checkIn,
        checkOut,
        ReservationStatus.PENDING,
        null,
        null,
        null,
        totalPrice);
  }

  protected PromoCodeDto createPromoCode(
      String code, DiscountType type, String value, Integer maxUses) {
    return promoCodeService.create(
        new PromoCodeDto(
            null,
            code,
            type,
            new BigDecimal(value),
            true,
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1),
            null,
            maxUses,
            null));
  }
}
