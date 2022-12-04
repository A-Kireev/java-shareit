package ru.practicum.shareit.unittests;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyLong;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTests {

  @Mock
  private RequestRepository requestRepository;
  @Mock
  private UserService userService;
  @Mock
  private ItemService itemService;

  @Test
  void createItemRequestTest() {
    var itemRequestService = new ItemRequestServiceImpl(requestRepository, userService, itemService);

    var requesterId = 1L;
    var inputItemRequestDto = ItemRequestDto.builder()
        .description("New item request")
        .build();
    var expectedItemRequest = ItemRequest.builder()
        .id(1L)
        .description(inputItemRequestDto.getDescription())
        .requesterId(requesterId)
        .createDateTime(LocalDateTime.now())
        .build();

    Mockito.when(requestRepository.save(Mockito.any())).thenReturn(expectedItemRequest);
    var expectedItemRequestDto = ItemRequestMapper.toItemRequestDto(expectedItemRequest);

    var itemRequestDto = itemRequestService.createItemRequest(requesterId, inputItemRequestDto);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(itemRequestDto)
            .usingRecursiveComparison()
            .isEqualTo(expectedItemRequestDto));
  }

  @Test
  void getUserItemRequestsTest() {
    var itemRequestService = new ItemRequestServiceImpl(requestRepository, userService, itemService);

    var requesterId = 1L;
    var inputItemRequestDto = ItemRequestDto.builder()
        .description("New item request")
        .build();
    var expectedItemRequest = ItemRequest.builder()
        .id(1L)
        .description(inputItemRequestDto.getDescription())
        .requesterId(requesterId)
        .createDateTime(LocalDateTime.now())
        .build();

    Mockito
        .when(requestRepository.findAllByRequesterId(requesterId, Sort.by("createDateTime").descending()))
        .thenReturn(List.of(expectedItemRequest));
    var expectedItemRequestDto = ItemRequestMapper.toItemRequestDto(expectedItemRequest);
    expectedItemRequestDto.setItems(Collections.emptyList());

    var itemRequestDto = itemRequestService.getUserItemRequests(requesterId);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(itemRequestDto.get(0))
            .usingRecursiveComparison()
            .isEqualTo(expectedItemRequestDto));
  }

  @Test
  void getItemRequestsTest() {
    var itemRequestService = new ItemRequestServiceImpl(requestRepository, userService, itemService);

    var notRequesterId = 1L;
    var inputItemRequestDto = ItemRequestDto.builder()
        .description("New item request")
        .build();
    var expectedItemRequest = ItemRequest.builder()
        .id(1L)
        .description(inputItemRequestDto.getDescription())
        .requesterId(notRequesterId)
        .createDateTime(LocalDateTime.now())
        .build();

    Mockito
        .when(requestRepository.findAllByRequesterIdNot(notRequesterId,
            PageRequest.of(0, 2, Sort.by("createDateTime").descending())))
        .thenReturn(List.of(expectedItemRequest));
    var expectedItemRequestDto = ItemRequestMapper.toItemRequestDto(expectedItemRequest);
    expectedItemRequestDto.setItems(Collections.emptyList());

    var itemRequestDto = itemRequestService.getItemRequests(notRequesterId, 0, 2);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(itemRequestDto.get(0))
            .usingRecursiveComparison()
            .isEqualTo(expectedItemRequestDto));
  }

  @Test
  void getItemRequestTest() {
    var itemRequestService = new ItemRequestServiceImpl(requestRepository, userService, itemService);

    var expectedItemRequest = ItemRequest.builder()
        .id(1L)
        .description("testItemRequest")
        .createDateTime(LocalDateTime.now())
        .build();

    var expectedItemDto = ItemDto.builder()
        .id(1L)
        .requestId(1L)
        .description("testItem")
        .isAvailable(true)
        .build();

    Mockito.when(requestRepository.findById(anyLong())).thenReturn(Optional.of(expectedItemRequest));
    Mockito.when(itemService.findItemByRequestId(anyLong())).thenReturn(List.of(expectedItemDto));

    var expectedItemRequestDto = ItemRequestMapper.toItemRequestDto(expectedItemRequest);
    expectedItemRequestDto.setItems(List.of(expectedItemDto));

    var itemRequestDto = itemRequestService.getItemRequest(1L, 1L);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(itemRequestDto)
            .usingRecursiveComparison()
            .isEqualTo(expectedItemRequestDto));
  }
}
