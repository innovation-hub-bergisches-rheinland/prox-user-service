package de.innovationhub.prox.userservice.domain.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

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
}