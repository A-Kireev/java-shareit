package ru.practicum.shareit.commonhandler;

import java.util.NoSuchElementException;
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
  public ErrorResponse handleItemFieldsException(final IncorrectItemFieldException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorResponse handleNoPermitsException(final NoPermitsException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handleDuplicateEmailException(final DuplicateEmailException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleBlankEmailException(final BlankEmailException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleUserDoesNotExistsException(final UserDoesNotExistsException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleNoSuchElementException(final NoSuchElementException e) {
    return new ErrorResponse(e.getMessage());
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalStateException(final IllegalStateException e) {
    return new ErrorResponse(e.getMessage());
  }
}
