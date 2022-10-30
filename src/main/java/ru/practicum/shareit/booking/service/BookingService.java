package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingCreateResponseDto;

public interface BookingService {

  BookingCreateResponseDto bookItem(long bookerId, BookingCreateRequestDto requestDto);
}
