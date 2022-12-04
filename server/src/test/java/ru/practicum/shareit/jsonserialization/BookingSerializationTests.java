package ru.practicum.shareit.jsonserialization;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;

@JsonTest
class BookingSerializationTests {

  @Autowired
  private JacksonTester<BookingCreateRequestDto> jacksonTester;
  private BookingCreateRequestDto bookingDto;

  @BeforeEach
  void setUp() {
    bookingDto = BookingCreateRequestDto.builder()
        .id(1L)
        .itemId(1L)
        .startDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .build();
  }

  @Test
  void itemDtoSerializationTest() throws IOException {
    JsonContent<BookingCreateRequestDto> json = jacksonTester.write(bookingDto);
    SoftAssertions.assertSoftly(softAssertions -> {
      assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
      assertThat(json).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStartDateTime().toString());
      assertThat(json).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEndDateTime().toString());
      assertThat(json).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
    });
  }

  @Test
  void itemDtoDeserializationTest() throws IOException {
    JsonContent<BookingCreateRequestDto> json = jacksonTester.write(bookingDto);
    BookingCreateRequestDto deserializedBookingDto = jacksonTester.parseObject(json.getJson());

    assertSoftly(softAssertions ->
        softAssertions.assertThat(deserializedBookingDto)
            .usingRecursiveComparison()
            .isEqualTo(bookingDto));
  }
}
