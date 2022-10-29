package ru.practicum.shareit.request.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ItemRequest {

  @Id
  private Long id;
  private String description;
  private Long requesterId;
  private LocalDateTime createDateTime;
}
