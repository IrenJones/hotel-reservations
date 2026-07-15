package com.hotelreservation.template.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservations")
@Getter
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "room_id", nullable = false)
  private Room room;

  @Setter
  @Column(name = "guest_name", nullable = false)
  private String guestName;

  @Setter
  @Column(name = "guest_email", nullable = false)
  private String guestEmail;

  @Setter
  @Column(name = "check_in_date", nullable = false)
  private LocalDate checkInDate;

  @Setter
  @Column(name = "check_out_date", nullable = false)
  private LocalDate checkOutDate;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private ReservationStatus status;

  @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal totalPrice;

  @Column(name = "subtotal_price", nullable = false, precision = 12, scale = 2)
  private BigDecimal subtotalPrice;

  @Column(name = "discount_amount", nullable = false, precision = 12, scale = 2)
  private BigDecimal discountAmount;

  @Column(name = "promo_code", length = 50)
  private String promoCode;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  protected Reservation() {}

  public Reservation(
      Room room,
      String guestName,
      String guestEmail,
      LocalDate checkInDate,
      LocalDate checkOutDate,
      ReservationStatus status,
      BigDecimal subtotalPrice,
      BigDecimal discountAmount,
      BigDecimal totalPrice,
      String promoCode) {
    this.room = room;
    this.guestName = guestName;
    this.guestEmail = guestEmail;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
    this.status = status != null ? status : ReservationStatus.PENDING;
    this.subtotalPrice = subtotalPrice;
    this.discountAmount = discountAmount;
    this.totalPrice = totalPrice;
    this.promoCode = promoCode;
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
