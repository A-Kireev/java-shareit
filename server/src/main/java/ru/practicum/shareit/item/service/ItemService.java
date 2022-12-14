package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingInfoDto;

public interface ItemService {

  ItemDto createItem(long userId, ItemDto itemDto);

  ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

  ItemWithBookingInfoDto getItem(long userId, Long itemId);

  List<ItemWithBookingInfoDto> getItems(Long ownerId, Integer from, Integer size);

  List<ItemDto> searchItem(String searchCriteria, Integer from, Integer size);

  List<ItemDto> findItemByRequestId(long requestId);

  CommentDto addComment(long userId, long itemId, CommentDto comment);
}
