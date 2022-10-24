package ru.practicum.shareit.user.dto;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {

  private Long id;
  private String name;
  @Email
  private String email;
}
