package ru.practicum.shareit.item;

import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

  private final ItemService itemService;

  @PostMapping
  public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto) {
    return itemService.createItem(userId, itemDto);
  }

  @PatchMapping("/{itemId}")
  public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
      @PathVariable long itemId,
      @RequestBody ItemDto itemDto) {
    return itemService.updateItem(userId, itemId, itemDto);
  }

  @GetMapping("/{itemId}")
  public ItemDto getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
    return itemService.getItem(userId, itemId);
  }

  @GetMapping
  public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
    return itemService.getItems(userId);
  }

  @GetMapping("/search")
  public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
      @RequestParam("text") String searchCriteria) {
    return itemService.searchItem(searchCriteria.toLowerCase(Locale.ROOT));
  }
}
