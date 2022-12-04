package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BookingShortInfo {

  private Long id;
  private Long bookerId;
  @JsonProperty("start")
  private LocalDateTime startDateTime;
  @JsonProperty("end")
  private LocalDateTime endDateTime;
}
