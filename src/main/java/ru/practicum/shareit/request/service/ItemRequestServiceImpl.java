package ru.practicum.shareit.request.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.service.UserService;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceImpl implements ItemRequestService {

  private final RequestRepository requestRepository;
  private final UserService userService;
  private final ItemService itemService;

  @Override
  public ItemRequestDto createItemRequest(long requesterId, ItemRequestDto requestDto) {
    userService.getUser(requesterId);
    requestDto.setCreated(LocalDateTime.now());
    var itemRequest = requestRepository.save(ItemRequestMapper.toItemRequest(requesterId, requestDto));
    return ItemRequestMapper.toItemRequestDto(itemRequest);
  }

  @Override
  public List<ItemRequestDto> getUserItemRequests(long userId) {
    userService.getUser(userId);
    var userRequests = requestRepository.findAllByRequesterId(userId, Sort.by("createDateTime").descending())
        .stream()
        .map(ItemRequestMapper::toItemRequestDto)
        .collect(Collectors.toList());
    userRequests.forEach(s -> s.setItems(itemService.findItemByRequestId(s.getId())));
    return userRequests;
  }

  @Override
  public List<ItemRequestDto> getItemRequests(long userId, Integer from, Integer size) {
    userService.getUser(userId);
    Sort sort = Sort.by("createDateTime").descending();
    Pageable pageable = from != null && size != null
        ? PageRequest.of(from, size, sort)
        : PageRequest.of(0, Integer.MAX_VALUE, sort);

    var userRequests = requestRepository.findAllByRequesterIdNot(userId, pageable);

    var userRequestsDto = userRequests.stream()
        .map(ItemRequestMapper::toItemRequestDto)
        .collect(Collectors.toList());
    userRequestsDto.forEach(s -> s.setItems(itemService.findItemByRequestId(s.getId())));

    return userRequestsDto;
  }

  @Override
  public ItemRequestDto getItemRequest(long userId, long requestId) {
    userService.getUser(userId);
    var itemRequestDto = ItemRequestMapper.toItemRequestDto(requestRepository.findById(requestId).orElseThrow());
    itemRequestDto.setItems(itemService.findItemByRequestId(itemRequestDto.getId()));
    return itemRequestDto;
  }
}
