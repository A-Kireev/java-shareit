package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.request.model.ItemRequest;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class Item {

  private Long id;
  private String name;
  private String description;
  private Boolean isAvailable;
  private Long ownerId;
  private ItemRequest request;
}
