package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

public class ItemMapper {

  public static ItemDto toItemDto(Item item) {
    return new ItemDto(
        item.getId(),
        item.getName(),
        item.getDescription(),
        item.getIsAvailable(),
        item.getRequestId()
    );
  }

  public static Item toItem(ItemDto itemDto, long userId) {
    return new Item(
        itemDto.getId(),
        itemDto.getName(),
        itemDto.getDescription(),
        itemDto.getIsAvailable(),
        userId,
        itemDto.getRequestId()
    );
  }

  public static ItemWithBookingInfoDto toItemDtoWithBookingInfoDto(Item item) {
    return new ItemWithBookingInfoDto(
        item.getId(),
        item.getName(),
        item.getDescription(),
        item.getIsAvailable()
    );
  }
}
