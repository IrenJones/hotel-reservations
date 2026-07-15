package com.hotelreservation.template.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.support.ReservationIntegrationTestSupport;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ReservationServiceTest extends ReservationIntegrationTestSupport {

  @Test
  void calculatesAndPersistsTotalPriceAsBookingSnapshot() {
    // Given
    Room room = createRoom("125.50");
    ReservationDto request =
        reservationRequest(room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 4));

    // When
    ReservationDto created = reservationService.create(request);
    room.setPricePerNight(new BigDecimal("200.00"));
    roomRepository.saveAndFlush(room);
    ReservationDto retrieved = reservationService.getById(created.id());

    // Then
    assertThat(created.subtotalPrice()).isEqualByComparingTo("376.50");
    assertThat(created.discountAmount()).isEqualByComparingTo("0.00");
    assertThat(created.totalPrice()).isEqualByComparingTo("376.50");
    assertThat(retrieved.totalPrice()).isEqualByComparingTo("376.50");
  }

  @Test
  void calculatesOneNightStay() {
    // Given
    Room room = createRoom("99.99");
    ReservationDto request =
        reservationRequest(room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2));

    // When
    ReservationDto created = reservationService.create(request);

    // Then
    assertThat(created.totalPrice()).isEqualByComparingTo("99.99");
  }

  @Test
  void rejectsClientSuppliedTotalPrice() {
    // Given
    ReservationDto request =
        reservationRequestWithServerPrices(
            1L, LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), new BigDecimal("1.00"));

    // When
    var violations = validator.validate(request);

    // Then
    assertThat(violations)
        .anySatisfy(
            violation -> {
              assertThat(violation.getPropertyPath().toString()).isEqualTo("totalPrice");
              assertThat(violation.getMessage())
                  .isEqualTo("totalPrice is calculated by the server");
            });
  }
}
