package de.innovationhub.prox.userservice.application.user.mapper;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.userservice.application.user.message.dto.UserDTO;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserMapperTest {

  @Test
  void toDto() {
    // Given
    var id = UUID.randomUUID();
    var user = new User(id);

    // When
    var dto = UserMapper.INSTANCE.toDto(user);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.id()).isEqualTo(id);
  }

  @Test
  void toReadResponse() {
    // Given
    var id = UUID.randomUUID();
    var dto = new UserDTO(id);

    // When
    var response = UserMapper.INSTANCE.toReadResponse(dto);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.user()).isNotNull();
    assertThat(response.user().id()).isEqualTo(id);
  }
}