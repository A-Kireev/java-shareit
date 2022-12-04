package ru.practicum.shareit.controller;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingInfoDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private ItemService itemService;

  @Autowired
  private MockMvc mvc;

  private User user;

  private ItemDto itemDto;

  private ItemWithBookingInfoDto itemWithBookingInfoDto;

  @BeforeEach
  void setUp() {
    user = new User(1L, "testUserName", "testUser@email.com");
    itemDto = ItemDto.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .build();
    itemWithBookingInfoDto = ItemWithBookingInfoDto.builder()
        .id(itemDto.getId())
        .description(itemDto.getDescription())
        .isAvailable(itemDto.getIsAvailable())
        .name(itemDto.getName())
        .build();
  }

  @Test
  void createItemTest() throws Exception {
    when(itemService.createItem(anyLong(), any())).thenReturn(itemDto);

    var response = mvc.perform(post("/items")
            .content(mapper.writeValueAsString(itemDto))
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ItemDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(itemDto));
  }

  @Test
  void updateItemTest() throws Exception {
    when(itemService.updateItem(anyLong(), anyLong(), any())).thenReturn(itemDto);

    var response = mvc.perform(patch("/items/{itemId}", itemDto.getId())
            .content(mapper.writeValueAsString(itemDto))
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ItemDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(itemDto));
  }

  @Test
  void getItemTest() throws Exception {
    when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemWithBookingInfoDto);

    var response = mvc.perform(get("/items/{itemId}", itemDto.getId())
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), ItemWithBookingInfoDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(itemWithBookingInfoDto));
  }

  @Test
  void getItemsTest() throws Exception {
    var items = List.of(itemWithBookingInfoDto);
    when(itemService.getItems(anyLong(), any(), any())).thenReturn(items);

    var response = mvc.perform(get("/items")
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    List<ItemWithBookingInfoDto> responseObject = mapper.readValue(response.getContentAsString(),
        new TypeReference<>() {
        });
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(items));
  }

  @Test
  void searchItemsTest() throws Exception {
    var items = List.of(itemDto);
    when(itemService.searchItem(anyString(), any(), any())).thenReturn(items);

    var response = mvc.perform(get("/items/search")
            .param("text", "searchedText")
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    List<ItemDto> responseObject = mapper.readValue(response.getContentAsString(),
        new TypeReference<>() {
        });
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(items));
  }

  @Test
  void addCommentTest() throws Exception {
    var comment = CommentDto.builder()
        .id(1L)
        .authorName("commentAuthorName")
        .created(LocalDateTime.now())
        .text("commentText")
        .build();
    when(itemService.addComment(anyLong(), anyLong(), any())).thenReturn(comment);

    var response = mvc.perform(post("/items/{itemId}/comment", itemDto.getId())
            .content(mapper.writeValueAsString(comment))
            .header("X-Sharer-User-Id", user.getId())
            .characterEncoding(StandardCharsets.UTF_8)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn().getResponse();
    var responseObject = mapper.readValue(response.getContentAsString(), CommentDto.class);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(responseObject)
            .usingRecursiveComparison()
            .isEqualTo(comment));
  }
}
