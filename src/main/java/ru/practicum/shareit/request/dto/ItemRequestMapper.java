package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.request.model.ItemRequest;

public class ItemRequestMapper {

  public static ItemRequest toItemRequest(long requesterId, ItemRequestDto itemRequestDto) {
    return ItemRequest.builder()
        .description(itemRequestDto.getDescription())
        .requesterId(requesterId)
        .createDateTime(itemRequestDto.getCreated())
        .build();
  }

  public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
    return ItemRequestDto.builder()
        .id(itemRequest.getId())
        .description(itemRequest.getDescription())
        .created(itemRequest.getCreateDateTime())
        .build();
  }
}
