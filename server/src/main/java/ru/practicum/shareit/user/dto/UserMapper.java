package ru.practicum.shareit.user.dto;

import lombok.NonNull;
import ru.practicum.shareit.user.model.User;

public class UserMapper {

  public static UserDto toUserDto(@NonNull User user) {
    return new UserDto(
        user.getId(),
        user.getName(),
        user.getEmail()
    );
  }

  public static User toUser(@NonNull UserDto userDto) {
    return new User(
        userDto.getId(),
        userDto.getName(),
        userDto.getEmail()
    );
  }
}
