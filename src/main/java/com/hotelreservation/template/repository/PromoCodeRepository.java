package com.hotelreservation.template.repository;

import com.hotelreservation.template.domain.PromoCode;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {
  boolean existsByCode(String code);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select p from PromoCode p where p.code = :code")
  Optional<PromoCode> findByCodeForUpdate(@Param("code") String code);
}
