package ru.practicum.shareit.jpa;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
class BookingJpaTests {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  void findAllCurrentBookingsByBookerIdTest() {
    var user = new User(null, "testUserName", "testUser@email.com");
    em.persist(user);

    var item = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .ownerId(user.getId())
        .build();
    em.persist(item);

    var booking = Booking.builder()
        .item(item)
        .booker(user)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .status(BookingStatus.WAITING)
        .build();
    em.persist(booking);

    var bookings = bookingRepository.findAllCurrentBookingsByBookerId(user.getId(), LocalDateTime.now(),
        Pageable.unpaged());

    assertSoftly(softAssertions -> {
      softAssertions.assertThat(bookings.size())
          .usingRecursiveComparison()
          .isEqualTo(1);

      softAssertions.assertThat(bookings.get(0))
          .usingRecursiveComparison()
          .isEqualTo(booking);
    });
  }

  @Test
  void findAllCurrentBookingsByItemsIdsTest() {
    var user = new User(null, "testUserName", "testUser@email.com");
    em.persist(user);

    var item = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .ownerId(user.getId())
        .build();
    em.persist(item);

    var booking = Booking.builder()
        .item(item)
        .booker(user)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .status(BookingStatus.WAITING)
        .build();
    em.persist(booking);

    var bookings = bookingRepository.findAllCurrentBookingsByItemsIds(List.of(item.getId()), LocalDateTime.now(),
        Pageable.unpaged());

    assertSoftly(softAssertions -> {
      softAssertions.assertThat(bookings.size())
          .usingRecursiveComparison()
          .isEqualTo(1);

      softAssertions.assertThat(bookings.get(0))
          .usingRecursiveComparison()
          .isEqualTo(booking);
    });
  }
}
