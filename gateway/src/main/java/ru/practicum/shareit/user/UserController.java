package ru.practicum.shareit.user;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserController {

  private final UserClient userClient;

  @PostMapping
  public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
    return userClient.createUser(userDto);
  }

  @PatchMapping("/{userId}")
  public ResponseEntity<Object> updateUser(@PathVariable long userId, @RequestBody @Valid UserDto userDto) {
    return userClient.updateUser(userId, userDto);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<Object> getUser(@PathVariable long userId) {
    return userClient.getUser(userId);
  }

  @GetMapping
  public ResponseEntity<Object> getUsers() {
    return userClient.getUsers();
  }

  @DeleteMapping("/{userId}")
  public void deleteUser(@PathVariable long userId) {
    userClient.deleteUser(userId);
  }
}
