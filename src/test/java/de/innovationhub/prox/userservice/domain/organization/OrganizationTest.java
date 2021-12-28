package de.innovationhub.prox.userservice.domain.organization;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.user.User;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrganizationTest {

  @DisplayName("Should be the only owner When new Organization is created")
  @Test
  void newOrganization_userIsOwner() {
    // Given
    var user = new User(UUID.randomUUID());

    // When
    var org = new Organization("Organization 123", user);

    // Then
    assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers().get(user).role()).isEqualTo(OrganizationRole.OWNER);
  }

  @DisplayName("Should throw Exception when creating orga with too long name")
  @Test
  void shouldThrowException_WhenCreatingOrganizationWithTooLongName() {
    // Given
    var name = "a".repeat(256);
    var owner = new User(UUID.randomUUID());

    // When
    // Then
    assertThrows(IllegalArgumentException.class, () -> new Organization(name, owner));
  }

  @DisplayName("Should throw Exception when creating orga with blank name")
  @Test
  void shouldThrowException_WhenCreatingOrganizationWithBlanlName() {
    // Given
    var name = "  \t \n";
    var owner = new User(UUID.randomUUID());

    // When
    // Then
    assertThrows(IllegalArgumentException.class, () -> new Organization(name, owner));
  }

  @DisplayName("Should throw Exception when setting too long name")
  @Test
  void shouldThrowException_WhenSettingTooLongName() {
    // Given
    var name = "a".repeat(256);
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);

    // When
    // Then
    assertThrows(IllegalArgumentException.class, () -> org.setName(name));
  }

  @DisplayName("Should throw Exception when setting blank name")
  @Test
  void shouldThrowException_WhenSettingBlankName() {
    // Given
    var name = "  \t \n";
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);

    // When
    // Then
    assertThrows(IllegalArgumentException.class, () -> org.setName(name));
  }
}
