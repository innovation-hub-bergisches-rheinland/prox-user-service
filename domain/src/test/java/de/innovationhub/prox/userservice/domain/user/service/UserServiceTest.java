package de.innovationhub.prox.userservice.domain.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.exception.AmbiguousPrincipalException;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
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
}