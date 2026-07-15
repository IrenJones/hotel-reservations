package com.hotelreservation.template.controller;

import com.hotelreservation.template.dto.ReservationDto;
import com.hotelreservation.template.service.ReservationService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

  private final ReservationService reservationService;

  public ReservationController(ReservationService reservationService) {
    this.reservationService = reservationService;
  }

  @GetMapping
  public List<ReservationDto> getAll(@RequestParam(required = false) Long roomId) {
    return roomId != null ? reservationService.getByRoom(roomId) : reservationService.getAll();
  }

  @GetMapping("/{id}")
  public ReservationDto getById(@PathVariable Long id) {
    return reservationService.getById(id);
  }

  @PostMapping
  public ResponseEntity<ReservationDto> create(@Valid @RequestBody ReservationDto dto) {
    ReservationDto created = reservationService.create(dto);
    return ResponseEntity.created(URI.create("/api/reservations/" + created.id())).body(created);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    reservationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
