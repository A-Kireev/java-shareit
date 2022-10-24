package ru.practicum.shareit.user.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.exception.BlankEmailException;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceImpl implements UserService {

  private final UserDao storage;

  @Override
  public UserDto createUser(UserDto userDto) {
    checkEmailPresents(userDto);
    checkEmailUniqueness(userDto);

    var user = storage.createUser(UserMapper.toUser(userDto));
    return UserMapper.toUserDto(user);
  }

  @Override
  public UserDto updateUser(long userId, UserDto userDto) {
    checkEmailUniqueness(userDto);
    var userPreviousVersion = storage.getUser(userId);
    var updatedUser = User.builder()
        .id(userId)
        .name(userDto.getName() != null ? userDto.getName() : userPreviousVersion.getName())
        .email(userDto.getEmail() != null ? userDto.getEmail() : userPreviousVersion.getEmail())
        .build();

    var user = storage.updateUser(updatedUser);
    return UserMapper.toUserDto(user);
  }

  @Override
  public UserDto getUser(long userId) {
    return storage.getUser(userId) != null ? UserMapper.toUserDto(storage.getUser(userId)) : null;
  }

  @Override
  public List<UserDto> getUsers() {
    return storage.getUsers().stream()
        .map(UserMapper::toUserDto)
        .collect(Collectors.toList());
  }

  @Override
  public void deleteUser(long userId) {
    storage.deleteUser(userId);
  }

  private void checkEmailUniqueness(UserDto userDto) {
    var isEmailAlreadyExists = storage.getUsers().stream()
        .map(User::getEmail)
        .collect(Collectors.toList())
        .contains(userDto.getEmail());

    if (isEmailAlreadyExists) {
      throw new DuplicateEmailException("Данный адрес электронной почты уже зарегистрирован");
    }
  }

  private void checkEmailPresents(UserDto userDto) {
    if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
      throw new BlankEmailException("Адрес электронной почты должен быть заполнен");
    }
  }
}
