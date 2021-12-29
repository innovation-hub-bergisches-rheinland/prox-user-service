package de.innovationhub.prox.userservice.infrastructure.user.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserJpaMapperTest {

  private UserJpaMapper userJpaMapper;

  @BeforeEach
  void setUp() {
    userJpaMapper = UserJpaMapper.INSTANCE;
  }

  @Test
  void should_map_User_to_UserJpa() {
    // Given
    var id = UUID.randomUUID();
    var user = new User(id);

    // When
    var jpa = userJpaMapper.toPersistence(user);

    // Then
    assertThat(jpa).isNotNull();
    assertThat(jpa.getId()).isEqualTo(id);
  }

  @Test
  void should_map_UserJpa_to_User() {
    // Given
    var id = UUID.randomUUID();
    var jpa = new UserJpa();
    jpa.setId(id);

    // When
    var user = userJpaMapper.toDomain(jpa);

    // Then
    assertThat(user).isNotNull();
    assertThat(user.getId()).isEqualTo(id);
  }
}