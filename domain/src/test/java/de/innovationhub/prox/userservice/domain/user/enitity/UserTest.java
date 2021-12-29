package de.innovationhub.prox.userservice.domain.user.enitity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void should_throw_exception_when_id_is_null() {
    assertThatThrownBy(() -> new Organization(null, "abc")).isInstanceOf(IllegalArgumentException.class);
  }
}