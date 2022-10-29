package ru.practicum.shareit.item.service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.exception.IncorrectItemFieldException;
import ru.practicum.shareit.item.exception.NoPermitsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserDoesNotExistsException;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceImpl implements ItemService {

  private final ItemRepository storage;
  private final UserService userService;

  @Override
  public ItemDto createItem(long userId, ItemDto itemDto) {
    checkUserExists(userId);
    checkFieldsFilled(itemDto);

    var item = storage.save(ItemMapper.toItem(itemDto, userId, null));
    return ItemMapper.toItemDto(item);
  }

  @Override
  public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
    var itemPreviousVersion = storage.findById(itemId).orElseThrow();

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

    var item = storage.save(updatedItem);
    return ItemMapper.toItemDto(item);
  }

  @Override
  public ItemDto getItem(long userId, Long itemId) {
    return ItemMapper.toItemDto(storage.findById(itemId).orElseThrow(NoSuchElementException::new));
  }

  @Override
  public List<ItemDto> getItems(Long ownerId) {
    return storage.findAllByOwnerId(ownerId).stream()
        .map(ItemMapper::toItemDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<ItemDto> searchItem(String searchCriteria) {
    if (StringUtils.isBlank(searchCriteria)) {
      return Collections.emptyList();
    }

    return storage.findAllByNameOrDescription(searchCriteria)
        .stream()
        .map(ItemMapper::toItemDto)
        .collect(Collectors.toList());
  }

  private void checkUserExists(long userId) {
    if (userService.getUser(userId) == null) {
      throw new UserDoesNotExistsException("User with id: " + userId + " doesn't exists");
    }
  }

  private void checkFieldsFilled(ItemDto itemDto) {
    var isNameFilledCorrectly = StringUtils.isNoneBlank(itemDto.getName());
    var isDescriptionFilledCorrectly = StringUtils.isNoneBlank(itemDto.getDescription());
    var isAvailableFilledCorrectly = itemDto.getIsAvailable() != null;

    if (!isNameFilledCorrectly || !isDescriptionFilledCorrectly || !isAvailableFilledCorrectly) {
      throw new IncorrectItemFieldException("Некорректно заполнены поля объекта item");
    }
  }
}
