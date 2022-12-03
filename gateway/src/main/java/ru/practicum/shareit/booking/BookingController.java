package ru.practicum.shareit.booking;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingFilter;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

  private final BookingClient bookingClient;

  @PostMapping
  public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long bookerId,
      @Valid @RequestBody BookingCreateRequestDto createRequestDto) {
    return bookingClient.bookItem(bookerId, createRequestDto);
  }

  @PatchMapping("/{bookingId}")
  public ResponseEntity<Object> decidingOnRequest(@RequestHeader("X-Sharer-User-Id") long userId,
      @PathVariable long bookingId,
      @RequestParam("approved") boolean isApproved) {
    return bookingClient.decidingOnRequest(userId, bookingId, isApproved);
  }

  @GetMapping("/{bookingId}")
  public ResponseEntity<Object> getBookingInfo(@RequestHeader("X-Sharer-User-Id") long userId,
      @PathVariable long bookingId) {
    return bookingClient.getBookingInfo(userId, bookingId);
  }

  @GetMapping
  public ResponseEntity<Object> getAllBookingInfo(@RequestHeader("X-Sharer-User-Id") long userId,
      @RequestParam(required = false, defaultValue = "ALL") BookingFilter state,
      @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
      @RequestParam(value = "size", required = false) @Positive Integer size) {
    return bookingClient.getAllBookingInfo(userId, state, from, size);
  }

  @GetMapping("/owner")
  public ResponseEntity<Object> getAllOwnerBookingInfo(@RequestHeader("X-Sharer-User-Id") long userId,
      @RequestParam(required = false, defaultValue = "ALL") BookingFilter state,
      @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
      @RequestParam(value = "size", required = false) @Positive Integer size) {
    return bookingClient.getAllOwnerBookingInfo(userId, state, from, size);
  }
}
