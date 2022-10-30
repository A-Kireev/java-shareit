package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingCreateResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

  private final BookingService bookingService;

  @PostMapping
  public BookingCreateResponseDto bookItem(@RequestHeader("X-Sharer-User-Id") long bookerId,
      @RequestBody BookingCreateRequestDto createRequestDto) {
    return bookingService.bookItem(bookerId, createRequestDto);
  }
}
