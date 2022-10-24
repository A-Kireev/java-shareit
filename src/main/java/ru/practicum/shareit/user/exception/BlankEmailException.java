package ru.practicum.shareit.user.exception;

public class BlankEmailException extends RuntimeException {

  public BlankEmailException(String message) {
    super(message);
  }
}
