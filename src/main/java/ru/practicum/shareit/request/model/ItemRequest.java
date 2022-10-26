package ru.practicum.shareit.request.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequest {

  private Long id;
  private String description;
  private Long requesterId;
  private LocalDateTime createDateTime;
}
