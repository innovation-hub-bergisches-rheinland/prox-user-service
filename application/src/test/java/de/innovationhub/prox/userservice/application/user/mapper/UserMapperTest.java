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
    var principal = UUID.randomUUID().toString();
    var user = new User(id, principal);

    // When
    var dto = UserMapper.INSTANCE.toDto(user);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.principal()).isEqualTo(principal);
  }

  @Test
  void toReadResponse() {
    // Given
    var principal = UUID.randomUUID().toString();
    var dto = new UserDTO(principal);

    // When
    var response = UserMapper.INSTANCE.toReadResponse(dto);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.user()).isNotNull();
    assertThat(response.user().principal()).isEqualTo(principal);
  }
}