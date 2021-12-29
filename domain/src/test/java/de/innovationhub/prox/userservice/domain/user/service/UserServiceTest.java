package de.innovationhub.prox.userservice.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.exception.AmbiguousPrincipalException;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationRole;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

  private UserRepository userRepository;
  private OrganizationRepository organizationRepository;
  private UserService userService;

  @BeforeEach
  void setUp() {
    this.userRepository = mock(UserRepository.class);
    this.organizationRepository = mock(OrganizationRepository.class);
    this.userService = new UserService(userRepository, organizationRepository);
  }

  @Test
  void should_create_and_return_user() {
    // Given
    var principal = UUID.randomUUID().toString();

    // When
    var createdUser = this.userService.create(principal);

    // Then
    assertThat(createdUser).isNotNull();
    assertThat(createdUser.getPrincipal()).isEqualTo(principal);
    verify(userRepository).create(any());
  }

  @Test
  void should_throw_if_principal_already_present() {
    // Given
    var principal = UUID.randomUUID().toString();
    when(this.userRepository.existByPrincipal(eq(principal))).thenReturn(true);

    // When
    // Then
    assertThatThrownBy(() -> this.userService.create(principal)).isInstanceOf(
        AmbiguousPrincipalException.class);
  }

  @Test
  void should_add_membership() {
    // Given
    var user = new User("max-mustermann");
    var organization = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG");
    when(this.userRepository.findByPrincipalOptional(eq("max-mustermann"))).thenReturn(Optional.of(user));
    when(this.organizationRepository.findByIdOptional(organization.getId())).thenReturn(Optional.of(organization));

    // When
    var updatedUser = this.userService.addMembership("max-mustermann", organization.getId(), OrganizationRole.MEMBER);

    // Then
    assertThat(updatedUser).isNotNull();
    assertThat(updatedUser.getMemberships().get(organization)).isNotNull();
    var membership = updatedUser.getMemberships().get(organization);
    assertThat(membership.role()).isEqualTo(OrganizationRole.MEMBER);
    verify(this.userRepository).update(eq("max-mustermann"), any());
  }
}