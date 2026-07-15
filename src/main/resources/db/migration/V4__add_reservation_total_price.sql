ALTER TABLE reservations ADD COLUMN total_price DECIMAL(12,2);

UPDATE reservations
SET total_price = (
    SELECT rooms.price_per_night
           * DATEDIFF('DAY', reservations.check_in_date, reservations.check_out_date)
    FROM rooms
    WHERE rooms.id = reservations.room_id
);

ALTER TABLE reservations ALTER COLUMN total_price SET NOT NULL;
ALTER TABLE reservations ADD CONSTRAINT chk_reservation_total_price CHECK (total_price > 0);
