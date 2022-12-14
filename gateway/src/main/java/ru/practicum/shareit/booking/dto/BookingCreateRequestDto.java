package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import javax.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.constraints.EndDateValidation;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
@EndDateValidation
public class BookingCreateRequestDto {

  private Long id;
  @Future
  @JsonProperty("start")
  private LocalDateTime startDateTime;
  @Future
  @JsonProperty("end")
  private LocalDateTime endDateTime;
  private Long itemId;
}
