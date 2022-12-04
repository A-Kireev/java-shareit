package ru.practicum.shareit.jpa;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;

@DataJpaTest
class ItemJpaTests {

  @Autowired
  private TestEntityManager em;

  @Autowired
  private ItemRepository itemRepository;

  @Test
  void findAllByNameOrDescriptionTest() {
    var user = new User(null, "testUserName", "testUser@email.com");
    var item = Item.builder()
        .name("itemName")
        .description("itemDescription")
        .isAvailable(true)
        .ownerId(user.getId())
        .build();

    em.persist(user);
    em.persist(item);

    var items = itemRepository.findAllByNameOrDescription("itemName", Pageable.unpaged());

    assertSoftly(softAssertions -> {
      softAssertions.assertThat(items.size())
          .usingRecursiveComparison()
          .isEqualTo(1);

      softAssertions.assertThat(items.get(0))
          .usingRecursiveComparison()
          .isEqualTo(item);
    });
  }
}
