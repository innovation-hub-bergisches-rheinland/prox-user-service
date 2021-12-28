package de.innovationhub.prox.userservice.infrastructure.user.repository;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class UserRepositoryImplTest {

  @Inject
  UserRepository userRepository;

  @Test
  @TestTransaction
  void findByIdOptional() {
    // Given
    var id = UUID.randomUUID();
    var user = new User(id);
    userRepository.save(user);

    // When
    var optResult = userRepository.findByIdOptional(id);

    // Then
    assertThat(optResult).isNotEmpty();
    var foundUser = optResult.get();
    assertThat(foundUser.getId()).isEqualTo(id);
  }

  @Test
  @TestTransaction
  void findByIdOptionalEmpty() {
    // Given
    var id = UUID.randomUUID();

    // When
    var optResult = userRepository.findByIdOptional(id);

    // Then
    assertThat(optResult).isEmpty();
  }

  @Test
  @TestTransaction
  void save() {
    // Given
    var id = UUID.randomUUID();
    var user = new User(id);

    // When
    userRepository.save(user);

    // Then
    var optResult = userRepository.findByIdOptional(id);
    assertThat(optResult).isNotEmpty();
    var foundUser = optResult.get();
    assertThat(foundUser.getId()).isEqualTo(id);
  }
}