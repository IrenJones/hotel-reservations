package com.hotelreservation.template.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.hotelreservation.template.support.MigrationTestDatabase;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ReservationPriceMigrationTest {

  @Test
  void backfillsExistingReservationFromRoomRateAndStayLength() throws Exception {
    // Given
    MigrationTestDatabase database = new MigrationTestDatabase();
    database.migrateTo("3");
    database.execute(
        """
                INSERT INTO hotels (name, address, city, star_rating)
                VALUES ('Migration Hotel', '1 Test Street', 'Testville', 4)
                """);
    database.execute(
        """
                INSERT INTO rooms (hotel_id, room_number, room_type, price_per_night, max_occupancy)
                VALUES (1, '101', 'DOUBLE', 125.50, 2)
                """);
    database.execute(
        """
                INSERT INTO reservations
                    (room_id, guest_name, guest_email, check_in_date, check_out_date, status)
                VALUES (1, 'Test Guest', 'guest@example.com', DATE '2026-08-01', DATE '2026-08-04', 'PENDING')
                """);

    // When
    database.migrateToLatest();
    BigDecimal totalPrice =
        database.queryBigDecimal(
            "SELECT total_price FROM reservations WHERE id = 1", "total_price");

    // Then
    assertThat(totalPrice).isEqualByComparingTo(new BigDecimal("376.50"));
  }
}
