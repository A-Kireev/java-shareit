package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

  public static Booking toBooking(long bookerId, BookingCreateRequestDto requestDto) {
    return toBooking(bookerId, requestDto, BookingStatus.WAITING);
  }

  public static Booking toBooking(long bookerId, BookingCreateRequestDto requestDto, BookingStatus status) {
    return Booking.builder()
        .bookerId(bookerId)
        .itemId(requestDto.getItemId())
        .startDateTime(requestDto.getStartDateTime())
        .endDateTime(requestDto.getEndDateTime())
        .status(status)
        .build();
  }

  public static BookingCreateResponseDto toBookingCreateResponseDto(Booking booking, User booker, Item item) {
    return BookingCreateResponseDto.builder()
        .id(booking.getId())
        .booker(booker)
        .item(item)
        .startDateTime(booking.getStartDateTime())
        .endDateTime(booking.getEndDateTime())
        .status(booking.getStatus())
        .build();
  }

  public static BookingCreateResponseDto toBookingCreateResponseDto(Booking booking) {
    return BookingCreateResponseDto.builder()
        .id(booking.getId())
        .booker(new User(booking.getBookerId(), null, null))
        .item(new Item(booking.getItemId(), null, null, false, null, null))
        .startDateTime(booking.getStartDateTime())
        .endDateTime(booking.getEndDateTime())
        .status(booking.getStatus())
        .build();
  }
}
