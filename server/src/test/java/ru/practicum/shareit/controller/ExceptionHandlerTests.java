package ru.practicum.shareit.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.commonhandler.ErrorResponse;
import ru.practicum.shareit.item.exception.NoPermitsException;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

@WebMvcTest(controllers = UserController.class)
class ExceptionHandlerTests {

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private UserService userService;

  @Autowired
  private MockMvc mvc;

  private UserDto userDto;

  @BeforeEach
  void setUp() {
    userDto = new UserDto(1L, "testUserName", "testUser@email.com");
  }

  @Test
  void createUserWithIllegalStateExceptionTest() throws Exception {
    when(userService.createUser(any()))
        .thenThrow(new IllegalStateException("Email address must not be empty"));

    var response = mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject.getError())
            .isEqualTo("Email address must not be empty"));
  }

  @Test
  void createUserWithSuchElementExceptionTest() throws Exception {
    when(userService.createUser(any()))
        .thenThrow(new NoSuchElementException("NoSuchElementException"));

    var response = mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject.getError())
            .isEqualTo("NoSuchElementException"));
  }

  @Test
  void createUserWithNoPermitsExceptionTest() throws Exception {
    when(userService.createUser(any()))
        .thenThrow(new NoPermitsException("NoPermitsException"));

    var response = mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject.getError())
            .isEqualTo("NoPermitsException"));
  }

  @Test
  void createUserWithIllegalArgumentExceptionTest() throws Exception {
    when(userService.createUser(any()))
        .thenThrow(new IllegalArgumentException("IllegalArgumentException"));

    var response = mvc.perform(post("/users")
            .content(mapper.writeValueAsString(userDto))
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is4xxClientError())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ErrorResponse.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject.getError())
            .isEqualTo("IllegalArgumentException"));
  }
}
