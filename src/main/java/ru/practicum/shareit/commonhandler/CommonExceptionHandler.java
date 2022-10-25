package ru.practicum.shareit.commonhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.IncorrectItemFieldException;
import ru.practicum.shareit.item.exception.NoPermitsException;
import ru.practicum.shareit.user.exception.BlankEmailException;
import ru.practicum.shareit.user.exception.DuplicateEmailException;
import ru.practicum.shareit.user.exception.UserDoesNotExistsException;

@RestControllerAdvice
public class CommonExceptionHandler {

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleItemFieldsException(final IncorrectItemFieldException e) {
    return e.getMessage();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public String handleNoPermitsException(final NoPermitsException e) {
    return e.getMessage();
  }

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
