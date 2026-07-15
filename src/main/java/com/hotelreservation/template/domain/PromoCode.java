package com.hotelreservation.template.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "promo_codes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PromoCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 50)
  private String code;

  @Enumerated(EnumType.STRING)
  @Column(name = "discount_type", nullable = false, length = 20)
  private DiscountType discountType;

  @Column(name = "discount_value", nullable = false, precision = 12, scale = 2)
  private BigDecimal discountValue;

  @Column(nullable = false)
  private boolean active;

  @Column(name = "valid_from")
  private LocalDate validFrom;

  @Column(name = "valid_until")
  private LocalDate validUntil;

  @Column(name = "minimum_subtotal", precision = 12, scale = 2)
  private BigDecimal minimumSubtotal;

  @Column(name = "max_uses")
  private Integer maxUses;

  @Column(name = "used_count", nullable = false)
  private int usedCount;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  public PromoCode(
      String code,
      DiscountType discountType,
      BigDecimal discountValue,
      boolean active,
      LocalDate validFrom,
      LocalDate validUntil,
      BigDecimal minimumSubtotal,
      Integer maxUses) {
    this.code = code;
    this.discountType = discountType;
    this.discountValue = discountValue;
    this.active = active;
    this.validFrom = validFrom;
    this.validUntil = validUntil;
    this.minimumSubtotal = minimumSubtotal;
    this.maxUses = maxUses;
  }

  public void incrementUsedCount() {
    usedCount++;
  }

  public void deactivate() {
    active = false;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }
}
