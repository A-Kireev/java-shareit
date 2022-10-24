package ru.practicum.shareit.user.exception;

public class UserDoesNotExistsException extends RuntimeException {

  public UserDoesNotExistsException(String message) {
    super(message);
  }
}
