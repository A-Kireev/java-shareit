package ru.practicum.shareit.booking.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  List<Booking> findAllByBookerId(long userId);

  List<Booking> findAllByBookerIdAndStatus(long bookerId, String status);
}
