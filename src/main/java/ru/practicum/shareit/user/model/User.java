package ru.practicum.shareit.user.model;

import javax.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class User {

  private Long id;
  private String name;
  @Email
  private String email;
}
