package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  List<Booking> findAllByBookerIdOrderByStartDateTimeDesc(long userId);

  List<Booking> findAllByBookerIdAndStartDateTimeBeforeOrderByStartDateTimeDesc(long userId,
      LocalDateTime localDate);

  @Query(value = "SELECT * "
      + "FROM bookings "
      + "WHERE start_date < ?1 "
      + "AND end_date > ?1 "
      + "ORDER BY start_date DESC", nativeQuery = true)
  List<Booking> findAllCurrentBookingsByBookerId(long userId, LocalDateTime localDate);

  List<Booking> findAllByBookerIdAndStartDateTimeAfterOrderByStartDateTimeDesc(long userId, LocalDateTime localDate);

  List<Booking> findAllByBookerIdAndStatusOrderByStartDateTimeDesc(long bookerId, BookingStatus status);
}
