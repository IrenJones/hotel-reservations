package com.hotelreservation.template.service;

import com.hotelreservation.template.config.ResourceNotFoundException;
import com.hotelreservation.template.domain.DiscountType;
import com.hotelreservation.template.domain.PromoCode;
import com.hotelreservation.template.dto.PromoCodeDto;
import com.hotelreservation.template.mapper.PromoCodeMapper;
import com.hotelreservation.template.repository.PromoCodeRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PromoCodeService {

  private final PromoCodeRepository promoCodeRepository;
  private final PromoCodeMapper promoCodeMapper;

  public PromoCodeService(
      PromoCodeRepository promoCodeRepository, PromoCodeMapper promoCodeMapper) {
    this.promoCodeRepository = promoCodeRepository;
    this.promoCodeMapper = promoCodeMapper;
  }

  public List<PromoCodeDto> getAll() {
    return promoCodeRepository.findAll().stream().map(promoCodeMapper::toDto).toList();
  }

  public PromoCodeDto getById(Long id) {
    return promoCodeMapper.toDto(findEntity(id));
  }

  public PromoCodeDto create(PromoCodeDto dto) {
    validate(dto);
    String code = normalize(dto.code());
    if (promoCodeRepository.existsByCode(code)) {
      throw new IllegalArgumentException("Promo code already exists: " + code);
    }
    PromoCode promoCode =
        new PromoCode(
            code,
            dto.discountType(),
            dto.discountValue(),
            dto.active() == null || dto.active(),
            dto.validFrom(),
            dto.validUntil(),
            dto.minimumSubtotal(),
            dto.maxUses());
    return promoCodeMapper.toDto(promoCodeRepository.save(promoCode));
  }

  @Transactional
  public void deactivate(Long id) {
    findEntity(id).deactivate();
  }

  static String normalize(String code) {
    return code.trim().toUpperCase(Locale.ROOT);
  }

  private void validate(PromoCodeDto dto) {
    if (dto.discountType() == DiscountType.PERCENTAGE
        && dto.discountValue().compareTo(new BigDecimal("100")) > 0) {
      throw new IllegalArgumentException("percentage discountValue must not exceed 100");
    }
    if (dto.validFrom() != null
        && dto.validUntil() != null
        && dto.validUntil().isBefore(dto.validFrom())) {
      throw new IllegalArgumentException("validUntil must not be before validFrom");
    }
  }

  private PromoCode findEntity(Long id) {
    return promoCodeRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Promo code not found: " + id));
  }
}
