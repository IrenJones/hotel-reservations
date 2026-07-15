package com.hotelreservation.template.repository;

import com.hotelreservation.template.domain.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {}
