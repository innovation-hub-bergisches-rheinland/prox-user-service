package de.innovationhub.prox.userservice.infrastructure.user.repository;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
class UserRepositoryImplTest {

  @Inject
  UserRepository userRepository;

  @Test
  void findByPrincipalOptional() {
    // Given
    var principal = UUID.randomUUID().toString();
    var user = new User(principal);
    userRepository.create(user);

    // When
    var optResult = userRepository.findByPrincipalOptional(principal);

    // Then
    assertThat(optResult).isNotEmpty();
    var foundUser = optResult.get();
    assertThat(foundUser.getPrincipal()).isEqualTo(principal);
  }

  @Test
  void findByPrincipalOptionalEmpty() {
    // Given
    var principal = UUID.randomUUID().toString();

    // When
    var optResult = userRepository.findByPrincipalOptional(principal);

    // Then
    assertThat(optResult).isEmpty();
  }

  @Test
  void save() {
    // Given
    var principal = UUID.randomUUID().toString();
    var user = new User(principal);

    // When
    userRepository.create(user);

    // Then
    var optResult = userRepository.findByPrincipalOptional(principal);
    assertThat(optResult).isNotEmpty();
    var foundUser = optResult.get();
    assertThat(foundUser.getPrincipal()).isEqualTo(principal);
  }

  @Test
  void shouldNotExistByPrincipalIfPrincipalNotPersisted() {
    // Given
    var principal = UUID.randomUUID().toString();

    // When
    var exist = userRepository.existByPrincipal(principal);

    // Then
    assertThat(exist).isFalse();
  }

  @Test
  void shouldExistByPrincipalIfPrincipalIsPersisted() {
    // Given
    var id = UUID.randomUUID();
    var principal = UUID.randomUUID().toString();
    var user = new User(principal);
    userRepository.create(user);

    // When
    var exist = userRepository.existByPrincipal(principal);

    // Then
    assertThat(exist).isTrue();
  }
}