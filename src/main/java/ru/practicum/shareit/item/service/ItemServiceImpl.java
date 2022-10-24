package ru.practicum.shareit.item.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.IncorrectItemFieldException;
import ru.practicum.shareit.item.exception.NoPermitsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserDoesNotExistsException;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {

  private final ItemDao storage;
  private final UserService userService;

  @Override
  public ItemDto createItem(long userId, ItemDto itemDto) {
    checkUserExists(userId);
    checkFieldsFilled(itemDto);

    var item = storage.createItem(ItemMapper.toItem(itemDto, userId, null));
    return ItemMapper.toItemDto(item);
  }

  @Override
  public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
    var itemPreviousVersion = storage.getItem(itemId);
    System.out.println(itemPreviousVersion);

    if (itemPreviousVersion.getOwnerId() != userId) {
      throw new NoPermitsException("Пользователь с id: " + userId + " не имеет прав на редактирование данной вещи");
    }

    var updatedItem = Item.builder()
        .id(itemId)
        .name(itemDto.getName() != null ? itemDto.getName() : itemPreviousVersion.getName())
        .description(itemDto.getDescription() != null ? itemDto.getDescription() : itemPreviousVersion.getDescription())
        .isAvailable(itemDto.getIsAvailable() != null ? itemDto.getIsAvailable() : itemPreviousVersion.getIsAvailable())
        .ownerId(itemPreviousVersion.getOwnerId())
        .request(itemPreviousVersion.getRequest())
        .build();

    var item = storage.updateItem(updatedItem);
    return ItemMapper.toItemDto(item);
  }

  @Override
  public ItemDto getItem(long userId, Long itemId) {
    return ItemMapper.toItemDto(storage.getItem(itemId));
  }

  @Override
  public List<ItemDto> getItems(Long ownerId) {
    return storage.getItems(ownerId).stream()
        .map(ItemMapper::toItemDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ItemDto> searchItem(String searchCriteria) {
    if (searchCriteria.isBlank()) {
      return Collections.emptyList();
    }

    return storage.searchItem(searchCriteria).stream()
        .map(ItemMapper::toItemDto)
        .collect(Collectors.toList());
  }

  private void checkUserExists(long userId) {
    if (userService.getUser(userId) == null) {
      throw new UserDoesNotExistsException("User with id: " + userId + " doesn't exists");
    }
  }

  private void checkFieldsFilled(ItemDto itemDto) {
    var isNameFilledCorrectly = itemDto.getName() != null && !itemDto.getName().isBlank();
    var isDescriptionFilledCorrectly = itemDto.getDescription() != null && !itemDto.getDescription().isBlank();
    var isAvailableFilledCorrectly = itemDto.getIsAvailable() != null;

    if (!isNameFilledCorrectly || !isDescriptionFilledCorrectly || !isAvailableFilledCorrectly) {
      throw new IncorrectItemFieldException("Некорректно заполнены поля объекта item");
    }
  }
}
