package com.hotelreservation.template.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "hotels")
@Getter
public class Hotel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Setter
  @Column(nullable = false)
  private String name;

  @Setter
  @Column(length = 500)
  private String address;

  @Setter
  @Column(nullable = false)
  private String city;

  @Setter
  @Column(name = "star_rating")
  private Integer starRating;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  protected Hotel() {}

  public Hotel(String name, String address, String city, Integer starRating) {
    this.name = name;
    this.address = address;
    this.city = city;
    this.starRating = starRating;
  }

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
  }
}
