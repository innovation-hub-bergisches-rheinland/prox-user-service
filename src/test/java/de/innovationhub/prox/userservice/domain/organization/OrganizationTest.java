package de.innovationhub.prox.userservice.domain.organization;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.Organization;
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
    assertThat(org.getOwners()).hasSize(1);
    assertThat(org.getOwners()).contains(user);
  }

  @DisplayName("Should be the only member When new Organization is created")
  @Test
  void newOrganization_userIsMember() {
    // Given
    var user = new User(UUID.randomUUID());

    // When
    var org = new Organization("Organization 123", user);

    // Then
    assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers()).contains(user);
  }

  @DisplayName("Should throw exception When adding owner who is not a member")
  @Test
  void shouldThrowException_WhenAddingOwner() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);
    var user = new User(UUID.randomUUID());

    // When
    // Then
    assertThrows(RuntimeException.class, () -> org.addOwner(user));
    assertThat(org.getOwners()).hasSize(1);
    assertThat(org.getOwners()).contains(owner);
  }

  @DisplayName("Should add owner When adding member")
  @Test
  void shouldAddOwner_WhenAddingMember() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);
    var user = new User(UUID.randomUUID());
    org.addMember(user);

    // When
    org.addOwner(user);

    // Then
    assertThat(org.getOwners()).hasSize(2);
    assertThat(org.getOwners()).contains(user, owner);
  }

  @DisplayName("Should add member When adding member")
  @Test
  void shouldAddMember_WhenAddingMember() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);
    var user = new User(UUID.randomUUID());

    // When
    org.addMember(user);

    // Then
    assertThat(org.getMembers()).hasSize(2);
    assertThat(org.getMembers()).contains(user, owner);
  }

  @DisplayName("Should remove owner When removing secondary owner")
  @Test
  void shouldRemoveOwner_WhenRemovingSecondaryOwner() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);
    var user = new User(UUID.randomUUID());
    org.addMember(user);
    org.addOwner(user);

    // When
    org.removeOwner(owner);

    // Then
    assertThat(org.getOwners()).hasSize(1);
    assertThat(org.getOwners()).contains(user);
    assertThat(org.getOwners()).doesNotContain(owner);
  }

  @DisplayName("Should throw Exception when removing last owner")
  @Test
  void shouldThrowException_WhenRemovingLastOwner() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);

    // When
    // Then
    assertThrows(RuntimeException.class, () -> org.removeOwner(owner));

    assertThat(org.getOwners()).hasSize(1);
    assertThat(org.getOwners()).contains(owner);
  }

  @DisplayName("Should remove member When removing member")
  @Test
  void shouldAddMember_WhenRemovingMember() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);
    var user = new User(UUID.randomUUID());
    org.addMember(user);

    // When
    org.removeMember(user);

    // Then
    assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers()).doesNotContain(user);
  }

  @DisplayName("Should throw Exception when removing member who is owner")
  @Test
  void shouldThrowException_WhenRemovingMemberWhoIsOwner() {
    // Given
    var owner = new User(UUID.randomUUID());
    var org = new Organization("Organization 123", owner);
    var user = new User(UUID.randomUUID());
    org.addMember(user);
    org.addOwner(user);

    // When
    // Then
    assertThrows(RuntimeException.class, () -> org.removeMember(user));

    assertThat(org.getOwners()).hasSize(2);
    assertThat(org.getOwners()).contains(user, user);
  }

  @DisplayName("Should throw Exception when creating orga with too long name")
  @Test
  void shouldThrowException_WhenCreatingOrganizationWithTooLongName() {
    // Given
    var name = "a".repeat(256);
    var owner = new User(UUID.randomUUID());

    // When
    // Then
    assertThrows(
      IllegalArgumentException.class,
      () -> new Organization(name, owner)
    );
  }

  @DisplayName("Should throw Exception when creating orga with blank name")
  @Test
  void shouldThrowException_WhenCreatingOrganizationWithBlanlName() {
    // Given
    var name = "  \t \n";
    var owner = new User(UUID.randomUUID());

    // When
    // Then
    assertThrows(
      IllegalArgumentException.class,
      () -> new Organization(name, owner)
    );
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
