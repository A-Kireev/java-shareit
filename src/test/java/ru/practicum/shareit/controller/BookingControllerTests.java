package ru.practicum.shareit.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateRequestDto;
import ru.practicum.shareit.booking.dto.BookingCreateResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.model.User;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTests {

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private BookingService bookingService;

  @Autowired
  private MockMvc mvc;

  private BookingCreateRequestDto bookingRequestDto;

  private BookingCreateResponseDto bookingCreateResponseDto;

  private User user;

  @BeforeEach
  void setUp() {
    bookingRequestDto = BookingCreateRequestDto.builder()
        .startDateTime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.SECONDS))
        .endDateTime(LocalDateTime.now().plusHours(5).truncatedTo(ChronoUnit.SECONDS))
        .build();
    bookingCreateResponseDto = BookingCreateResponseDto.builder()
        .id(1L)
        .startDateTime(bookingRequestDto.getStartDateTime())
        .endDateTime(bookingRequestDto.getEndDateTime())
        .build();
    user = new User(1L, "testUserName", "testUser@email.com");
  }

  @Test
  void bookItemTest() throws Exception {
    when(bookingService.bookItem(anyLong(), any())).thenReturn(bookingCreateResponseDto);

    var response = mvc.perform(post("/bookings")
            .content(mapper.writeValueAsString(bookingRequestDto))
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), BookingCreateResponseDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(bookingCreateResponseDto));
  }

  @Test
  void decidingOnRequestTest() throws Exception {
    when(bookingService.decidingOnRequest(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingCreateResponseDto);

    var response = mvc.perform(patch("/bookings/{bookingId}", bookingCreateResponseDto.getId())
            .param("approved", "true")
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), BookingCreateResponseDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(bookingCreateResponseDto));
  }

  @Test
  void getBookingInfoTest() throws Exception {
    when(bookingService.getBookingInfo(anyLong(), anyLong())).thenReturn(bookingCreateResponseDto);

    var response = mvc.perform(get("/bookings/{bookingId}", bookingCreateResponseDto.getId())
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), BookingCreateResponseDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(bookingCreateResponseDto));
  }

  @Test
  void getAllBookingInfoTest() throws Exception {
    var expectedBookings = List.of(bookingCreateResponseDto);
    when(bookingService.getAllBookingInfo(anyLong(), any(), any(), any()))
        .thenReturn(expectedBookings);

    var response = mvc.perform(get("/bookings")
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    List<BookingCreateResponseDto> responseObject = mapper.readValue(response.getContentAsString(),
        new TypeReference<>() {
        });
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(expectedBookings));
  }

  @Test
  void getAllOwnerBookingInfoTest() throws Exception {
    var expectedBookings = List.of(bookingCreateResponseDto);
    when(bookingService.getAllOwnerBookingInfo(anyLong(), any(), any(), any()))
        .thenReturn(expectedBookings);

    var response = mvc.perform(get("/bookings/owner")
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    List<BookingCreateResponseDto> responseObject = mapper.readValue(response.getContentAsString(),
        new TypeReference<>() {
        });
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(expectedBookings));
  }
}
