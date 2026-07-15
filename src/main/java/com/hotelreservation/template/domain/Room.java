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
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
public class Room {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "hotel_id", nullable = false)
  private Hotel hotel;

  @Setter
  @Column(name = "room_number", nullable = false, length = 20)
  private String roomNumber;

  @Setter
  @Enumerated(EnumType.STRING)
  @Column(name = "room_type", nullable = false, length = 20)
  private RoomType roomType;

  @Setter
  @Column(name = "price_per_night", nullable = false, precision = 10, scale = 2)
  private BigDecimal pricePerNight;

  @Setter
  @Column(name = "max_occupancy", nullable = false)
  private Integer maxOccupancy;

  protected Room() {}

  public Room(
      Hotel hotel,
      String roomNumber,
      RoomType roomType,
      BigDecimal pricePerNight,
      Integer maxOccupancy) {
    this.hotel = hotel;
    this.roomNumber = roomNumber;
    this.roomType = roomType;
    this.pricePerNight = pricePerNight;
    this.maxOccupancy = maxOccupancy;
  }
}
