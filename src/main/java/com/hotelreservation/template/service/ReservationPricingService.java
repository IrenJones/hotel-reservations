package com.hotelreservation.template.service;

import com.hotelreservation.template.config.InvalidPromoCodeException;
import com.hotelreservation.template.domain.DiscountType;
import com.hotelreservation.template.domain.PromoCode;
import com.hotelreservation.template.repository.PromoCodeRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
public class ReservationPricingService {

  private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

  private final PromoCodeRepository promoCodeRepository;

  public ReservationPricingService(PromoCodeRepository promoCodeRepository) {
    this.promoCodeRepository = promoCodeRepository;
  }

  public ReservationPrice calculate(BigDecimal pricePerNight, long nights, String requestedCode) {
    BigDecimal subtotal = pricePerNight.multiply(BigDecimal.valueOf(nights));
    if (requestedCode == null) {
      return new ReservationPrice(subtotal, BigDecimal.ZERO.setScale(2), subtotal, null);
    }

    String code = PromoCodeService.normalize(requestedCode);
    PromoCode promoCode =
        promoCodeRepository
            .findByCodeForUpdate(code)
            .orElseThrow(() -> new InvalidPromoCodeException("Invalid promo code: " + code));
    validateEligibility(promoCode, subtotal, LocalDate.now());

    BigDecimal discount = calculateDiscount(promoCode, subtotal);
    promoCode.incrementUsedCount();
    return new ReservationPrice(
        subtotal, discount, subtotal.subtract(discount), promoCode.getCode());
  }

  private void validateEligibility(PromoCode promoCode, BigDecimal subtotal, LocalDate today) {
    if (!promoCode.isActive()) {
      throw new InvalidPromoCodeException("Promo code is inactive");
    }
    if (promoCode.getValidFrom() != null && today.isBefore(promoCode.getValidFrom())) {
      throw new InvalidPromoCodeException("Promo code is not valid yet");
    }
    if (promoCode.getValidUntil() != null && today.isAfter(promoCode.getValidUntil())) {
      throw new InvalidPromoCodeException("Promo code has expired");
    }
    if (promoCode.getMinimumSubtotal() != null
        && subtotal.compareTo(promoCode.getMinimumSubtotal()) < 0) {
      throw new InvalidPromoCodeException("Reservation does not meet the minimum subtotal");
    }
    if (promoCode.getMaxUses() != null && promoCode.getUsedCount() >= promoCode.getMaxUses()) {
      throw new InvalidPromoCodeException("Promo code usage limit has been reached");
    }
  }

  private BigDecimal calculateDiscount(PromoCode promoCode, BigDecimal subtotal) {
    BigDecimal discount =
        promoCode.getDiscountType() == DiscountType.PERCENTAGE
            ? subtotal
                .multiply(promoCode.getDiscountValue())
                .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP)
            : promoCode.getDiscountValue();
    return discount.min(subtotal).setScale(2, RoundingMode.HALF_UP);
  }

  public record ReservationPrice(
      BigDecimal subtotalPrice,
      BigDecimal discountAmount,
      BigDecimal totalPrice,
      String promoCode) {}
}
