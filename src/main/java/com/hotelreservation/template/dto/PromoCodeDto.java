package com.hotelreservation.template.dto;

import com.hotelreservation.template.domain.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record PromoCodeDto(
    Long id,
    @NotBlank(message = "code is required")
        @Size(max = 50, message = "code must not exceed 50 characters")
        String code,
    @NotNull(message = "discountType is required") DiscountType discountType,
    @NotNull(message = "discountValue is required")
        @Positive(message = "discountValue must be positive")
        BigDecimal discountValue,
    Boolean active,
    LocalDate validFrom,
    LocalDate validUntil,
    @PositiveOrZero(message = "minimumSubtotal must not be negative") BigDecimal minimumSubtotal,
    @Positive(message = "maxUses must be positive") Integer maxUses,
    @Null(message = "usedCount is managed by the server") Integer usedCount) {}
