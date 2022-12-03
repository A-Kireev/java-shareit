package ru.practicum.shareit.request;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestController {

  private final ItemRequestClient itemRequestClient;

  @PostMapping
  public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") long requesterId,
      @RequestBody @Valid ItemRequestDto itemRequestDto) {
    return itemRequestClient.createItemRequest(requesterId, itemRequestDto);
  }

  @GetMapping
  public ResponseEntity<Object> getUserItemRequests(@RequestHeader("X-Sharer-User-Id") long requesterId) {
    return itemRequestClient.getUserItemRequests(requesterId);
  }

  @GetMapping("/all")
  public ResponseEntity<Object> getItemRequests(@RequestHeader("X-Sharer-User-Id") long requesterId,
      @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
      @RequestParam(value = "size", required = false) @PositiveOrZero Integer size) {
    return itemRequestClient.getItemRequests(requesterId, from, size);
  }

  @GetMapping("/{requestId}")
  public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") long requesterId,
      @PathVariable long requestId) {
    return itemRequestClient.getItemRequest(requesterId, requestId);
  }
}
