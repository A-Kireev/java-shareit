package ru.practicum.shareit.integration;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
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
import ru.practicum.shareit.request.model.ItemRequest;
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
  void getItemsPageTest() {
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

    var targetItems = itemService.getItems(user.getId(), 0, 2);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetItems.size())
            .isEqualTo(sourceItems.size()));
  }

  @Test
  void getItemTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    em.persist(user);

    var sourceItem = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .ownerId(user.getId())
        .build();
    em.persist(sourceItem);
    em.flush();

    var targetItem = itemService.getItem(user.getId(), sourceItem.getId());
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetItem)
            .usingRecursiveComparison()
            .ignoringFields("id", "lastBooking", "nextBooking", "comments")
            .isEqualTo(sourceItem));
  }

  @Test
  void searchItemsTest() {
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

    var targetItems = itemService.searchItem("itemName", null, null);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetItems.size())
            .isEqualTo(sourceItems.size()));
  }

  @Test
  void searchItemsPageTest() {
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

    var targetItems = itemService.searchItem("itemName", 0, 2);
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetItems.size())
            .isEqualTo(sourceItems.size()));
  }

  @Test
  void findItemByRequestIdTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    em.persist(user);

    var request = ItemRequest.builder()
        .description("requestDescription")
        .createDateTime(LocalDateTime.now())
        .requesterId(user.getId())
        .build();
    em.persist(request);

    var sourceItem = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .ownerId(user.getId())
        .requestId(request.getId())
        .build();
    em.persist(sourceItem);
    em.flush();

    var targetItems = itemService.findItemByRequestId(sourceItem.getRequestId());
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetItems.get(0))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(sourceItem));
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

  @Test
  void updateItemTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    em.persist(user);

    var item = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .ownerId(user.getId())
        .build();
    em.persist(item);

    var itemDto = ItemDto.builder()
        .id(item.getId())
        .name("itemNewName")
        .description("itemNewDescription")
        .isAvailable(false)
        .build();
    itemService.updateItem(user.getId(), item.getId(), itemDto);

    TypedQuery<Item> query = em.createQuery("Select i from items i where i.name = :name", Item.class);
    var updateItem = query.setParameter("name", itemDto.getName()).getSingleResult();

    assertSoftly(softAssertions ->
        softAssertions.assertThat(ItemMapper.toItemDto(updateItem))
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(itemDto));
  }
}
