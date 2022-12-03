package ru.practicum.shareit.item;

import java.util.Locale;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemController {

  private final ItemClient itemClient;

  @PostMapping
  public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
      @RequestBody ItemDto itemDto) {
    return itemClient.createItem(userId, itemDto);
  }

  @PatchMapping("/{itemId}")
  public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
      @PathVariable long itemId,
      @RequestBody ItemDto itemDto) {
    return itemClient.updateItem(userId, itemId, itemDto);
  }

  @GetMapping("/{itemId}")
  public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
    return itemClient.getItem(userId, itemId);
  }

  @GetMapping
  public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
      @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
      @RequestParam(value = "size", required = false) @PositiveOrZero Integer size) {
    return itemClient.getItems(userId, from, size);
  }

  @GetMapping("/search")
  public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
      @RequestParam("text") String searchCriteria,
      @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
      @RequestParam(value = "size", required = false) @PositiveOrZero Integer size) {
    return itemClient.searchItem(searchCriteria.toLowerCase(Locale.ROOT), userId, from, size);
  }

  @PostMapping("/{itemId}/comment")
  public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
      @PathVariable long itemId,
      @Valid @RequestBody CommentDto comment) {
    return itemClient.addComment(userId, itemId, comment);
  }
}
