package ru.practicum.shareit.integration;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserTests {

  private final EntityManager em;
  private final UserService userService;

  @Test
  void getUserTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    em.persist(user);
    em.flush();

    var targetUsers = userService.getUser(user.getId());
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetUsers)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(user));
  }

  @Test
  void getUsersTest() {
    var user = new User(null, "authorName", "mail@mail.com");
    em.persist(user);
    em.flush();

    var targetUsers = userService.getUsers();
    assertSoftly(softAssertions ->
        softAssertions.assertThat(targetUsers.size())
            .isEqualTo(1));
  }
}
