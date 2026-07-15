package com.hotelreservation.template.controller;

import com.hotelreservation.template.dto.RoomDto;
import com.hotelreservation.template.service.RoomService;
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
@RequestMapping("/api/rooms")
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @GetMapping
  public List<RoomDto> getAll(@RequestParam(required = false) Long hotelId) {
    return hotelId != null ? roomService.getByHotel(hotelId) : roomService.getAll();
  }

  @GetMapping("/{id}")
  public RoomDto getById(@PathVariable Long id) {
    return roomService.getById(id);
  }

  @PostMapping
  public ResponseEntity<RoomDto> create(@Valid @RequestBody RoomDto dto) {
    RoomDto created = roomService.create(dto);
    return ResponseEntity.created(URI.create("/api/rooms/" + created.id())).body(created);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    roomService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
