package ru.practicum.shareit.booking.model;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class FilterValueConverter implements Converter<String, BookingFilter> {

  @Override
  public BookingFilter convert(String s) {
    try {
      return BookingFilter.valueOf(s.toUpperCase());
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Unknown state: " + s);
    }
  }
}
