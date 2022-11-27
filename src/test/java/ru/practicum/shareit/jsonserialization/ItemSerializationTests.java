package ru.practicum.shareit.jsonserialization;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.io.IOException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

@JsonTest
class ItemSerializationTests {

  @Autowired
  private JacksonTester<ItemDto> jacksonTester;
  ItemDto itemDto;

  @BeforeEach
  void setUp() {
    itemDto = ItemDto.builder()
        .id(1L)
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .requestId(1L)
        .build();
  }

  @Test
  void itemDtoSerializationTest() throws IOException {
    JsonContent<ItemDto> json = jacksonTester.write(itemDto);
    SoftAssertions.assertSoftly(softAssertions -> {
      assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(1);
      assertThat(json).extractingJsonPathStringValue("$.name").isEqualTo("itemName");
      assertThat(json).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
      assertThat(json).extractingJsonPathNumberValue("$.requestId").isEqualTo(1);
    });
  }

  @Test
  void itemDtoDeserializationTest() throws IOException {
    JsonContent<ItemDto> json = jacksonTester.write(itemDto);
    ItemDto deserializedItemDto = jacksonTester.parseObject(json.getJson());

    assertSoftly(softAssertions ->
        softAssertions.assertThat(deserializedItemDto)
            .usingRecursiveComparison()
            .isEqualTo(itemDto));
  }
}
