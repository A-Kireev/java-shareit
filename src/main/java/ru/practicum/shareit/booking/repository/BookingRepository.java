package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

public interface BookingRepository extends JpaRepository<Booking, Long> {

  List<Booking> findAllByBookerId(long userId, Pageable pageable);

  List<Booking> findAllByBookerIdAndEndDateTimeBefore(long userId,
      LocalDateTime localDate, Pageable pageable);

  @Query(value = "SELECT b "
      + "FROM Booking b "
      + "WHERE start_date < ?2 "
      + "AND end_date > ?2 "
      + "AND booker_id = ?1 "
      + "ORDER BY start_date DESC")
  List<Booking> findAllCurrentBookingsByBookerId(long userId, LocalDateTime localDate, Pageable pageable);

  List<Booking> findAllByBookerIdAndStartDateTimeAfter(long userId, LocalDateTime localDate, Pageable pageable);

  List<Booking> findAllByBookerIdAndStatus(long bookerId, BookingStatus status, Pageable pageable);

  List<Booking> findAllByItemIdIn(List<Long> ids, Pageable pageable);

  @Query(value = "SELECT b "
      + "FROM Booking b "
      + "WHERE start_date < ?2 "
      + "AND end_date > ?2 "
      + "AND item_id IN (?1) "
      + "ORDER BY start_date DESC")
  List<Booking> findAllCurrentBookingsByItemsIds(List<Long> ids, LocalDateTime localDate, Pageable pageable);

  List<Booking> findAllByItemIdInAndEndDateTimeBefore(List<Long> ids, LocalDateTime localDate, Pageable pageable);

  List<Booking> findAllByItemIdInAndStartDateTimeAfter(List<Long> ids, LocalDateTime localDate, Pageable pageable);

  List<Booking> findAllByItemIdInAndStatus(List<Long> ids, BookingStatus status, Pageable pageable);

  Booking findByItemIdAndEndDateTimeBeforeOrderByEndDateTimeDesc(long itemId, LocalDateTime localDate);

  Booking findByItemIdAndStartDateTimeAfterOrderByEndDateTimeAsc(long itemId, LocalDateTime localDate);

  boolean existsByBookerIdAndItemIdAndEndDateTimeBefore(long bookerId, long itemId, LocalDateTime dateTime);
}
