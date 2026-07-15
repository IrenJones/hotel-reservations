package com.hotelreservation.template.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.hotelreservation.template.config.InvalidPromoCodeException;
import com.hotelreservation.template.domain.DiscountType;
import com.hotelreservation.template.domain.Room;
import com.hotelreservation.template.dto.PromoCodeDto;
import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.support.ReservationIntegrationTestSupport;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PromoCodeReservationTest extends ReservationIntegrationTestSupport {

  @Test
  void appliesPercentageCodeAndStoresNormalizedPricingSnapshot() {
    // Given
    Room room = createRoom("125.50");
    PromoCodeDto promoCode = createPromoCode(" summer20 ", DiscountType.PERCENTAGE, "20", null);
    ReservationDto request =
        reservationRequest(
            room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 4), "summer20");

    // When
    ReservationDto created = reservationService.create(request);

    // Then
    assertThat(created.promoCode()).isEqualTo("SUMMER20");
    assertThat(created.subtotalPrice()).isEqualByComparingTo("376.50");
    assertThat(created.discountAmount()).isEqualByComparingTo("75.30");
    assertThat(created.totalPrice()).isEqualByComparingTo("301.20");
    assertThat(promoCodeService.getById(promoCode.id()).usedCount()).isOne();
  }

  @Test
  void capsFixedDiscountAtSubtotal() {
    // Given
    Room room = createRoom("50.00");
    createPromoCode("FREE", DiscountType.FIXED_AMOUNT, "100.00", null);
    ReservationDto request =
        reservationRequest(
            room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), "FREE");

    // When
    ReservationDto created = reservationService.create(request);

    // Then
    assertThat(created.discountAmount()).isEqualByComparingTo("50.00");
    assertThat(created.totalPrice()).isEqualByComparingTo("0.00");
  }

  @Test
  void rejectsCodeAfterUsageLimitIsReached() {
    // Given
    Room room = createRoom("100.00");
    createPromoCode("ONCE", DiscountType.PERCENTAGE, "10", 1);
    ReservationDto request =
        reservationRequest(
            room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), "ONCE");
    reservationService.create(request);

    // When / Then
    assertThatThrownBy(() -> reservationService.create(request))
        .isInstanceOf(InvalidPromoCodeException.class)
        .hasMessage("Promo code usage limit has been reached");
  }

  @Test
  void rejectsExpiredAndMinimumSubtotalCodes() {
    // Given
    Room room = createRoom("50.00");
    promoCodeService.create(
        new PromoCodeDto(
            null,
            "EXPIRED",
            DiscountType.PERCENTAGE,
            new BigDecimal("10"),
            true,
            LocalDate.now().minusDays(2),
            LocalDate.now().minusDays(1),
            null,
            null,
            null));
    promoCodeService.create(
        new PromoCodeDto(
            null,
            "MINIMUM",
            DiscountType.FIXED_AMOUNT,
            new BigDecimal("5"),
            true,
            null,
            null,
            new BigDecimal("100.00"),
            null,
            null));

    ReservationDto expiredRequest =
        reservationRequest(
            room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), "EXPIRED");
    ReservationDto minimumRequest =
        reservationRequest(
            room.getId(), LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 2), "MINIMUM");

    // When / Then
    assertThatThrownBy(() -> reservationService.create(expiredRequest))
        .isInstanceOf(InvalidPromoCodeException.class)
        .hasMessage("Promo code has expired");
    assertThatThrownBy(() -> reservationService.create(minimumRequest))
        .isInstanceOf(InvalidPromoCodeException.class)
        .hasMessage("Reservation does not meet the minimum subtotal");
  }
}
