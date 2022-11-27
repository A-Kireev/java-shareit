package ru.practicum.shareit.integration;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemTests {

  private final EntityManager em;
  private final ItemService itemService;

  @Test
  void getItemsTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    var sourceItems = List.of(
        ItemDto.builder()
            .name("itemName")
            .description("itemDescription")
            .isAvailable(true)
            .build(),
        ItemDto.builder()
            .name("secondItemName")
            .description("secondItemDescription")
            .isAvailable(true)
            .build()
    );

    em.persist(user);
    for (var item : sourceItems) {
      var entity = ItemMapper.toItem(item, user.getId());
      em.persist(entity);
    }
    em.flush();

    var targetItems = itemService.getItems(user.getId(), null, null);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetItems.size())
            .isEqualTo(sourceItems.size()));
  }

  @Test
  void createItemTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    var itemDto = ItemDto.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .build();

    em.persist(user);
    itemService.createItem(user.getId(), itemDto);

    TypedQuery<Item> query = em.createQuery("Select i from items i where i.name = :name", Item.class);
    var item = query.setParameter("name", itemDto.getName()).getSingleResult();

    assertSoftly(softAssertions ->
        softAssertions.assertThat(ItemMapper.toItemDto(item))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(itemDto));
  }
}
