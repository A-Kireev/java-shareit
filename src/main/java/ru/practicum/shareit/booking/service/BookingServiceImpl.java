package ru.practicum.shareit.booking.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingCreateResponseDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.NoPermitsException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceImpl implements BookingService {

  private final BookingRepository bookingRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

  @Override
  public BookingCreateResponseDto bookItem(long bookerId, BookingCreateRequestDto requestDto) {
    var booker = userRepository.findById(bookerId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + bookerId + " doesn't exists"));
    var item = itemRepository.findById(requestDto.getItemId())
        .orElseThrow(() -> new NoSuchElementException("Item with id: " + requestDto.getItemId() + " doesn't exists"));
    if (!item.getIsAvailable()) {
      throw new IllegalStateException("Item with id: " + item.getId() + " is not available");
    }

    var booking = bookingRepository.save(BookingMapper.toBooking(bookerId, requestDto));
    return BookingMapper.toBookingCreateResponseDto(booking, booker, item);
  }

  @Override
  public BookingCreateResponseDto decidingOnRequest(long userId, long bookingId, boolean isApproved) {
    var booker = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + " doesn't exists"));
    var booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new NoSuchElementException("Booking with id: " + bookingId + " doesn't exists"));
    var item = itemRepository.findById(booking.getItemId()).orElseThrow();

    if (userId != item.getOwnerId()) {
      throw new IllegalStateException("User with id " + userId + " not item owner");
    }

    booking.setStatus(isApproved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
    booking = bookingRepository.save(booking);
    return BookingMapper.toBookingCreateResponseDto(booking, booker, item);
  }

  @Override
  public BookingCreateResponseDto getBookingInfo(long userId, long bookingId) {
    var booker = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id: " + userId + " doesn't exists"));
    var booking = bookingRepository.findById(bookingId)
        .orElseThrow(() -> new NoSuchElementException("Booking with id: " + bookingId + " doesn't exists"));
    var item = itemRepository.findById(booking.getItemId()).orElseThrow();

    if (userId != item.getOwnerId() || userId != booking.getBookerId()) {
      throw new NoPermitsException("User with id " + userId + " has no permits");
    }

    return BookingMapper.toBookingCreateResponseDto(booking, booker, item);
  }

  @Override
  public List<BookingCreateResponseDto> getAllBookingInfo(long userId, String state) {
    var bookings = "ALL".equals(state)
        ? bookingRepository.findAllByBookerIdOrderByStartDateTimeDesc(userId)
        : bookingRepository.findAllByBookerIdAndStatusOrderByStartDateTimeDesc(userId, BookingStatus.valueOf(state).name());

    return bookings.stream()
        .map(BookingMapper::toBookingCreateResponseDto)
        .collect(Collectors.toList());
  }
}
