package ru.practicum.shareit.item.service;

import java.util.List;
import ru.practicum.shareit.item.dto.ItemDto;

public interface ItemService {

  ItemDto createItem(long userId, ItemDto itemDto);

  ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

  ItemDto getItem(long userId, Long itemId);

  List<ItemDto> getItems(Long ownerId);

  List<ItemDto> searchItem(String searchCriteria);
}
