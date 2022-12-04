package ru.practicum.shareit.unittests;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.BookingFilter;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class BookingServiceTests {

  @Mock
  private BookingRepository bookingRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ItemRepository itemRepository;

  @Test
  void bookItemTest() {
    var bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

    var bookerId = 1L;
    var bookingRequestDto = BookingCreateRequestDto.builder()
        .itemId(1L)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .build();
    var booking = BookingMapper.toBooking(bookerId, bookingRequestDto);
    var user = new User(bookerId, "testUserName", "testUser@email.com");
    var item = Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(3L)
        .build();

    Mockito.when(userRepository.findById(bookerId)).thenReturn(Optional.of(user));
    Mockito.when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
    Mockito.when(bookingRepository.save(any())).thenReturn(booking);

    var actualBooking = bookingService.bookItem(bookerId, bookingRequestDto);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(actualBooking)
            .usingRecursiveComparison()
            .isEqualTo(BookingMapper.toBookingCreateResponseDto(booking)));
  }

  @Test
  void decidingOnRequestTest() {
    var bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

    var userId = 1L;
    var bookingId = 1L;
    var bookingRequestDto = BookingCreateRequestDto.builder()
        .itemId(1L)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .build();
    var booking = BookingMapper.toBooking(userId, bookingRequestDto);
    booking.setItem(Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build());

    Mockito.when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
    Mockito.when(bookingRepository.save(any())).thenReturn(booking);

    var actualBooking = bookingService.decidingOnRequest(userId, bookingId, true);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(actualBooking)
            .usingRecursiveComparison()
            .isEqualTo(BookingMapper.toBookingCreateResponseDto(booking)));
  }

  @Test
  void getBookingInfoTest() {
    var bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

    var userId = 1L;
    var bookingRequestDto = BookingCreateRequestDto.builder()
        .id(1L)
        .itemId(1L)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .build();
    var booking = BookingMapper.toBooking(userId, bookingRequestDto);
    booking.setItem(Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build());
    booking.setBooker(new User(userId, "testUserName", "testUser@email.com"));

    Mockito.when(bookingRepository.findById(bookingRequestDto.getId())).thenReturn(Optional.of(booking));

    var actualBooking = bookingService.getBookingInfo(userId, bookingRequestDto.getId());
    assertSoftly(softAssertions ->
        softAssertions.assertThat(actualBooking)
            .usingRecursiveComparison()
            .isEqualTo(BookingMapper.toBookingCreateResponseDto(booking)));
  }

  @Test
  void getAllBookingInfoTest() {
    var bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

    var userId = 1L;
    var bookingRequestDto = BookingCreateRequestDto.builder()
        .id(1L)
        .itemId(1L)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .build();
    var booking = BookingMapper.toBooking(userId, bookingRequestDto);
    booking.setItem(Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build());
    booking.setBooker(new User(userId, "testUserName", "testUser@email.com"));

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(booking.getBooker()));
    Mockito.when(bookingRepository.findAllByBookerId(anyLong(), any())).thenReturn(List.of(booking));

    var actualBooking = bookingService.getAllBookingInfo(userId, BookingFilter.ALL, null, null);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(actualBooking)
            .usingRecursiveComparison()
            .isEqualTo(List.of(BookingMapper.toBookingCreateResponseDto(booking))));
  }

  @Test
  void getAllOwnerBookingInfoTest() {
    var bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);

    var userId = 1L;
    var bookingRequestDto = BookingCreateRequestDto.builder()
        .id(1L)
        .itemId(1L)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .build();
    var booking = BookingMapper.toBooking(userId, bookingRequestDto);
    booking.setItem(Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build());
    booking.setBooker(new User(userId, "testUserName", "testUser@email.com"));

    Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(booking.getBooker()));
    Mockito.when(itemRepository.findAllByOwnerId(anyLong(), any())).thenReturn(List.of(booking.getItem()));
    Mockito.when(bookingRepository.findAllByItemIdIn(anyList(), any())).thenReturn(List.of(booking));

    var actualBooking = bookingService.getAllOwnerBookingInfo(userId, BookingFilter.ALL, null, null);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(actualBooking)
            .usingRecursiveComparison()
            .isEqualTo(List.of(BookingMapper.toBookingCreateResponseDto(booking))));
  }
}
