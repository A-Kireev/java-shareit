package ru.practicum.shareit.booking.model;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Booking {

  private Long id;
  private LocalDateTime startDateTime;
  private LocalDateTime endDateTime;
  private Long itemId;
  private Long bookerId;
  private BookingStatus status;
}
