package de.innovationhub.prox.userservice.user;

import de.innovationhub.prox.userservice.user.entity.UserMapper;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;

class UserMapperTest {
  private UserMapper mapper = UserMapper.INSTANCE;

  @Test
  void shouldUseUsernameIfFirstAndLastNameIsNull() {
    // Given
    var easyRandom = new EasyRandom();
    var userRepresentation = easyRandom.nextObject(UserRepresentation.class);
    userRepresentation.setFirstName(null);
    userRepresentation.setLastName(null);

    // When
    var mappingResult = this.mapper.parseName(userRepresentation);

    // Then
    Assertions.assertThat(mappingResult).isEqualTo(userRepresentation.getUsername());
  }

  @Test
  void shouldConcatenateFirstAndLastName() {
    // Given
    var easyRandom = new EasyRandom();
    var userRepresentation = easyRandom.nextObject(UserRepresentation.class);

    // When
    var mappingResult = this.mapper.parseName(userRepresentation);

    // Then
    Assertions.assertThat(mappingResult)
        .isEqualTo(userRepresentation.getFirstName() + " " + userRepresentation.getLastName());
  }
}
