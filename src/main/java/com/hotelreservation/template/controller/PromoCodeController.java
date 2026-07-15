package com.hotelreservation.template.controller;

import com.hotelreservation.template.dto.PromoCodeDto;
import com.hotelreservation.template.service.PromoCodeService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/promocodes")
public class PromoCodeController {

  private final PromoCodeService promoCodeService;

  public PromoCodeController(PromoCodeService promoCodeService) {
    this.promoCodeService = promoCodeService;
  }

  @GetMapping
  public List<PromoCodeDto> getAll() {
    return promoCodeService.getAll();
  }

  @GetMapping("/{id}")
  public PromoCodeDto getById(@PathVariable Long id) {
    return promoCodeService.getById(id);
  }

  @PostMapping
  public ResponseEntity<PromoCodeDto> create(@Valid @RequestBody PromoCodeDto dto) {
    PromoCodeDto created = promoCodeService.create(dto);
    return ResponseEntity.created(URI.create("/api/promocodes/" + created.id())).body(created);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deactivate(@PathVariable Long id) {
    promoCodeService.deactivate(id);
    return ResponseEntity.noContent().build();
  }
}
