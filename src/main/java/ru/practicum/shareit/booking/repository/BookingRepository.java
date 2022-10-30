package ru.practicum.shareit.booking.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingExt;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  List<BookingExt> findAllByBookerIdOrderByStartDateTimeDesc(long userId);

  List<BookingExt> findAllByBookerIdAndStatusOrderByStartDateTimeDesc(long bookerId, String status);
}
