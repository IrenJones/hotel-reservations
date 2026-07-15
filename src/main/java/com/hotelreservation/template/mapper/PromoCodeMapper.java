package com.hotelreservation.template.mapper;

import com.hotelreservation.template.domain.PromoCode;
import com.hotelreservation.template.dto.PromoCodeDto;
import org.springframework.stereotype.Component;

@Component
public class PromoCodeMapper {

  public PromoCodeDto toDto(PromoCode promoCode) {
    return new PromoCodeDto(
        promoCode.getId(),
        promoCode.getCode(),
        promoCode.getDiscountType(),
        promoCode.getDiscountValue(),
        promoCode.isActive(),
        promoCode.getValidFrom(),
        promoCode.getValidUntil(),
        promoCode.getMinimumSubtotal(),
        promoCode.getMaxUses(),
        promoCode.getUsedCount());
  }
}
