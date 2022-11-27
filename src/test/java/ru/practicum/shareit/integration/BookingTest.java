package ru.practicum.shareit.integration;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingFilter;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingTest {

  private final EntityManager em;
  private final BookingService bookingService;

  @Test
  void getAllBookingInfoTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    var item = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .build();
    var sourceBookings = List.of(
        Booking.builder()
            .item(item)
            .booker(user)
            .startDateTime(LocalDateTime.now())
            .endDateTime(LocalDateTime.now())
            .build()
    );

    em.persist(user);
    em.persist(item);
    for (var booking : sourceBookings) {
      em.persist(booking);
    }
    em.flush();

    var targetBookings = bookingService.getAllBookingInfo(user.getId(), BookingFilter.ALL, null, null);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetBookings.size())
            .isEqualTo(sourceBookings.size()));
  }

  @Test
  void getAllOwnerBookingInfoTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    em.persist(user);
    var item = Item.builder()
        .name("itemName")
        .ownerId(user.getId())
        .description("itemDescription")
        .isAvailable(true)
        .build();
    var sourceBookings = List.of(
        Booking.builder()
            .item(item)
            .startDateTime(LocalDateTime.now())
            .endDateTime(LocalDateTime.now())
            .build()
    );

    em.persist(item);
    for (var booking : sourceBookings) {
      em.persist(booking);
    }
    em.flush();

    var targetBookings = bookingService.getAllOwnerBookingInfo(user.getId(), BookingFilter.ALL, null, null);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetBookings.size())
            .isEqualTo(sourceBookings.size()));
  }
}
