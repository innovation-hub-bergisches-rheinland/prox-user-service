package de.innovationhub.prox.userservice.domain.organization.entity;

import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OrganizationTest {

  @Test
  void should_throw_exception_when_id_is_null() {
    assertThatThrownBy(() -> new Organization(null, "abc")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_throw_exception_when_name_is_null() {
    assertThatThrownBy(() -> new Organization(UUID.randomUUID(), null)).isInstanceOf(IllegalArgumentException.class);

    var org = new Organization(UUID.randomUUID(), "Musterfirma");
    assertThatThrownBy(() -> org.setName(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_throw_exception_when_name_is_empty() {
    assertThatThrownBy(() -> new Organization(UUID.randomUUID(), "")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new Organization(UUID.randomUUID(), "    \n \t     ")).isInstanceOf(IllegalArgumentException.class);

    var org = new Organization(UUID.randomUUID(), "Musterfirma");
    assertThatThrownBy(() -> org.setName("")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> org.setName("    \n \t     ")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_throw_exception_when_name_is_longer_than_255_chars() {
    assertThatCode(() -> new Organization(UUID.randomUUID(), "a".repeat(255))).doesNotThrowAnyException();
    assertThatThrownBy(() -> new Organization(UUID.randomUUID(), "a".repeat(256))).isInstanceOf(IllegalArgumentException.class);

    var org = new Organization(UUID.randomUUID(), "Musterfirma");
    assertThatCode(() -> org.setName("a".repeat(255))).doesNotThrowAnyException();
    assertThatThrownBy(() -> org.setName("a".repeat(256))).isInstanceOf(IllegalArgumentException.class);
  }

}
