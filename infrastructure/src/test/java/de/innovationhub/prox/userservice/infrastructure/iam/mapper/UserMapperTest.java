package de.innovationhub.prox.userservice.infrastructure.iam.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.infrastructure.iam.dto.UserResponseDto;
import de.innovationhub.prox.userservice.infrastructure.persistence.organization.OrganizationJpaMapper;
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
    assertThat(mappingResult).isEqualTo(userRepresentation.getUsername());
  }

  @Test
  void shouldConcatenateFirstAndLastName() {
    // Given
    var easyRandom = new EasyRandom();
    var userRepresentation = easyRandom.nextObject(UserRepresentation.class);

    // When
    var mappingResult = this.mapper.parseName(userRepresentation);

    // Then
    assertThat(mappingResult)
        .isEqualTo(userRepresentation.getFirstName() + " " + userRepresentation.getLastName());
  }
}
