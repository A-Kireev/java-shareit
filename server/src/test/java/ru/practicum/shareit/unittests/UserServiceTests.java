package ru.practicum.shareit.unittests;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

  @Mock
  private UserRepository userRepository;

  @Test
  void createUserTest() {
    var userService = new UserServiceImpl(userRepository);

    var inputUserDto = new UserDto(null, "testUserName", "testUser@email.com");
    var expectedUser = User.builder()
        .id(1L)
        .name(inputUserDto.getName())
        .email(inputUserDto.getEmail())
        .build();

    Mockito.when(userRepository.save(Mockito.any())).thenReturn(expectedUser);

    var user = userService.createUser(inputUserDto);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(user)
            .usingRecursiveComparison()
            .isEqualTo(UserMapper.toUserDto(expectedUser)));
  }

  @Test
  void updateUserTest() {
    var userService = new UserServiceImpl(userRepository);

    var inputUserDto = new UserDto(1L, "testUserName", "testUser@email.com");
    var previousVersionUser = User.builder()
        .id(1L)
        .name("oldName")
        .email("oldEmail")
        .build();
    var expectedUser = User.builder()
        .id(1L)
        .name(inputUserDto.getName())
        .email(inputUserDto.getEmail())
        .build();

    Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(previousVersionUser));
    Mockito.when(userRepository.save(Mockito.any())).thenReturn(expectedUser);

    var user = userService.updateUser(inputUserDto.getId(), inputUserDto);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(user)
            .usingRecursiveComparison()
            .isEqualTo(UserMapper.toUserDto(expectedUser)));
  }

  @Test
  void getUserTest() {
    var userService = new UserServiceImpl(userRepository);

    var expectedUser = User.builder()
        .id(1L)
        .name("userName")
        .email("userEmail@mail.com")
        .build();

    Mockito.when(userRepository.findById(expectedUser.getId())).thenReturn(Optional.of(expectedUser));

    var user = userService.getUser(expectedUser.getId());
    assertSoftly(softAssertions ->
        softAssertions.assertThat(user)
            .usingRecursiveComparison()
            .isEqualTo(UserMapper.toUserDto(expectedUser)));
  }

  @Test
  void getUsersTest() {
    var userService = new UserServiceImpl(userRepository);

    var expectedUsers = List.of(User.builder()
        .id(1L)
        .name("userName")
        .email("userEmail@mail.com")
        .build());

    Mockito.when(userRepository.findAll()).thenReturn(expectedUsers);

    var user = userService.getUsers();
    assertSoftly(softAssertions ->
        softAssertions.assertThat(user)
            .usingRecursiveComparison()
            .isEqualTo(expectedUsers));
  }

  @Test
  void deleteUserTest() {
    var userService = new UserServiceImpl(userRepository);

    var userId = 1L;
    userService.deleteUser(userId);

    Mockito.verify(userRepository, Mockito.times(1))
        .deleteById(userId);
  }
}
