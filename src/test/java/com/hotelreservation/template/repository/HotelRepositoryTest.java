package com.hotelreservation.template.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hotelreservation.template.domain.Hotel;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HotelRepositoryTest {

  @Autowired private HotelRepository hotelRepository;

  @Test
  void savesAndRetrievesHotel() {
    Hotel hotel = new Hotel("Grand Test Hotel", "1 Test Street", "Testville", 4);

    Hotel saved = hotelRepository.save(hotel);
    assertThat(saved.getId()).isNotNull();

    Optional<Hotel> found = hotelRepository.findById(saved.getId());
    assertThat(found).isPresent();
    assertThat(found.get().getName()).isEqualTo("Grand Test Hotel");
    assertThat(found.get().getCity()).isEqualTo("Testville");
  }
}
