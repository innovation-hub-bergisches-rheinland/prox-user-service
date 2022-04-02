package de.innovationhub.prox.userservice.user.service;

import static org.assertj.core.api.Assertions.*;

import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/** Runs against the pre-configured keycloak dev service. */
@QuarkusTest
class KeycloakServiceTest {

  @Inject KeycloakUserIdentityService keycloakService;

  @Test
  void shouldFindAliceWithId() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");

    // When
    var alice = keycloakService.findById(aliceId);

    // Then
    Assertions.assertThat(alice)
        .isNotEmpty()
        .get()
        .extracting("id", "username")
        .containsExactly(aliceId.toString(), "alice");
  }

  @Test
  void shouldNotFindWithRandomizedId() {
    // Given
    var randomId = UUID.randomUUID();

    // When
    var result = keycloakService.findById(randomId);

    // Then
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  void shouldFindByUsername() {
    // Given
    var searchQuery = "bob";

    // When
    var result = keycloakService.search(searchQuery);

    // Then
    Assertions.assertThat(result)
        .isNotEmpty()
        .extracting("id", "username")
        .contains(tuple("ed0b4a07-2612-4571-a9ab-27e13ce752f1", "bob"));
  }

  @Test
  void shouldFindByEmail() {
    // Given
    var searchQuery = "julianbbraden@cuvox.de";

    // When
    var result = keycloakService.searchByMail(searchQuery);

    // Then
    Assertions.assertThat(result)
        .isNotEmpty()
        .extracting("id", "username", "firstName", "lastName")
        .contains(tuple("64788f0d-a954-4898-bfda-7498aae2b271", "forgisell", "Julian", "Braden"));
  }

  @Test
  void shouldNotFindByPartialEmail() {
    // Given
    var searchQuery = "julianbbraden";

    // When
    var result = keycloakService.searchByMail(searchQuery);

    // Then
    assertThat(result).isEmpty();
  }

  @Test
  void shouldFindByFirstName() {
    // Given
    var searchQuery = "Julian";

    // When
    var result = keycloakService.search(searchQuery);

    // Then
    Assertions.assertThat(result)
        .isNotEmpty()
        .extracting("id", "username", "firstName", "lastName")
        .contains(tuple("64788f0d-a954-4898-bfda-7498aae2b271", "forgisell", "Julian", "Braden"));
  }

  @Test
  void shouldFindByLastName() {
    // Given
    var searchQuery = "Braden";

    // When
    var result = keycloakService.search(searchQuery);

    // Then
    Assertions.assertThat(result)
        .isNotEmpty()
        .extracting("id", "username", "firstName", "lastName")
        .contains(tuple("64788f0d-a954-4898-bfda-7498aae2b271", "forgisell", "Julian", "Braden"));
  }

  @Test
  void shouldFindByFullName() {
    // Given
    var searchQuery = "Julian Braden";

    // When
    var result = keycloakService.search(searchQuery);

    // Then
    Assertions.assertThat(result)
        .isNotEmpty()
        .extracting("id", "username", "firstName", "lastName")
        .contains(tuple("64788f0d-a954-4898-bfda-7498aae2b271", "forgisell", "Julian", "Braden"));
  }
}
