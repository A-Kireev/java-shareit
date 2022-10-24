package ru.practicum.shareit.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  public String handleDuplicateEmailException(final DuplicateEmailException e) {
    return e.getMessage();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleBlankEmailException(final BlankEmailException e) {
    return e.getMessage();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleUserDoesNotExistsException(final UserDoesNotExistsException e) {
    return e.getMessage();
  }
}
