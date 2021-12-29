package de.innovationhub.prox.userservice.domain.user.enitity;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void should_throw_exception_when_principal_is_null() {
    assertThatThrownBy(() -> new User(null)).isInstanceOf(IllegalArgumentException.class);
  }
}