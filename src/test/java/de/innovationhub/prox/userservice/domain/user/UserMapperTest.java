package de.innovationhub.prox.userservice.domain.user;

import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.userservice.application.user.mapper.UserMapper;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  @Test
  void shouldMapUserToGetDto() {
    // Given
    var id  = UUID.randomUUID();
    var user = new User(id);

    // When
    var userDto = UserMapper.INSTANCE.toGetDto(user);

    // Then
    assertThat(userDto).isNotNull();
    assertThat(userDto.id()).isEqualTo(id);
  }

  @Test
  void shouldMapUserJpaToUser() {
    // Given
    var id = UUID.randomUUID();
    var userJpa = new UserJpa(id);

    // When
    var user = UserMapper.INSTANCE.toDomain(userJpa);

    // Then
    assertThat(user).isNotNull();
    assertThat(user.getId()).isEqualTo(id);
  }
}