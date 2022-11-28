package ru.practicum.shareit.unittests;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class ItemServiceTests {

  @Mock
  private ItemRepository itemRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private BookingRepository bookingRepository;
  @Mock
  private CommentRepository commentRepository;

  @Test
  void createItemTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var userId = 1L;
    var inputItemDto = ItemDto.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .build();
    var expectedItem = ItemMapper.toItem(inputItemDto, userId);

    Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
    Mockito.when(itemRepository.save(any())).thenReturn(expectedItem);

    var item = itemService.createItem(userId, inputItemDto);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(item)
            .usingRecursiveComparison()
            .isEqualTo(ItemMapper.toItemDto(expectedItem)));
  }

  @Test
  void updateItemTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var userId = 1L;
    var previousItem = Item.builder()
        .id(1L)
        .name("oldItemName")
        .description("oldItemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build();
    var inputItemDto = ItemDto.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .build();
    var expectedItem = ItemMapper.toItem(inputItemDto, userId);

    Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(previousItem));
    Mockito.when(itemRepository.save(any())).thenReturn(expectedItem);

    var item = itemService.updateItem(userId, inputItemDto.getId(), inputItemDto);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(item)
            .usingRecursiveComparison()
            .isEqualTo(ItemMapper.toItemDto(expectedItem)));
  }

  @Test
  void getItemTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var userId = 1L;
    var expectedItem = Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build();
    var expectedItemWithBookingInfo = ItemMapper.toItemDtoWithBookingInfoDto(expectedItem);
    expectedItemWithBookingInfo.setComments(Collections.emptyList());

    Mockito.when(itemRepository.findById(anyLong())).thenReturn(Optional.of(expectedItem));

    var item = itemService.getItem(userId, expectedItem.getId());
    assertSoftly(softAssertions ->
        softAssertions.assertThat(item)
            .usingRecursiveComparison()
            .isEqualTo(expectedItemWithBookingInfo));
  }

  @Test
  void getItemsTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var userId = 1L;
    var expectedItem = Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build();
    var expectedItemWithBookingInfo = ItemMapper.toItemDtoWithBookingInfoDto(expectedItem);

    Mockito.when(itemRepository.findAllByOwnerId(anyLong(), any()))
        .thenReturn(List.of(expectedItem));

    var item = itemService.getItems(userId, 0, 2);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(item)
            .usingRecursiveComparison()
            .isEqualTo(List.of(expectedItemWithBookingInfo)));
  }

  @Test
  void searchItemsTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var userId = 1L;
    var expectedItem = Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .ownerId(userId)
        .build();
    var expectedDto = ItemMapper.toItemDto(expectedItem);

    Mockito.when(itemRepository.findAllByNameOrDescription(anyString(), any())).thenReturn(List.of(expectedItem));

    var item = itemService.searchItem(expectedItem.getName(), 0, 2);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(item)
            .usingRecursiveComparison()
            .isEqualTo(List.of(expectedDto)));
  }

  @Test
  void getItemByRequestIdTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var requestId = 1L;
    var expectedItem = Item.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(requestId)
        .ownerId(1L)
        .build();
    var expectedItemDto = ItemMapper.toItemDto(expectedItem);

    Mockito.when(itemRepository.findAllByRequestId(anyLong())).thenReturn(List.of(expectedItem));

    var item = itemService.findItemByRequestId(requestId);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(item)
            .usingRecursiveComparison()
            .isEqualTo(List.of(expectedItemDto)));
  }

  @Test
  void addCommentTest() {
    var itemService = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository);

    var userId = 1L;
    var itemId = 1L;
    var inputCommentDto = CommentDto.builder()
        .text("itemComment")
        .authorName("authorName")
        .created(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
        .build();
    var expectedComment = CommentMapper.toComment(inputCommentDto, userId, itemId);

    Mockito.when(userRepository.findById(anyLong()))
        .thenReturn(Optional.of(new User(userId, "authorName", "mail@mail.com")));
    Mockito.when(bookingRepository.existsByBookerIdAndItemIdAndEndDateTimeBefore(anyLong(), anyLong(), any()))
        .thenReturn(true);
    Mockito.when(commentRepository.save(any())).thenReturn(expectedComment);

    var comment = itemService.addComment(userId, itemId, inputCommentDto);
    comment.setCreated(comment.getCreated().truncatedTo(ChronoUnit.MINUTES));
    assertSoftly(softAssertions ->
        softAssertions.assertThat(comment)
            .usingRecursiveComparison()
            .isEqualTo(inputCommentDto));
  }
}
