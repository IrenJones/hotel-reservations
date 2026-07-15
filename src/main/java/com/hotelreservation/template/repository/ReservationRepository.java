package com.hotelreservation.template.repository;

import com.hotelreservation.template.domain.Reservation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
  List<Reservation> findByRoomId(Long roomId);
}
