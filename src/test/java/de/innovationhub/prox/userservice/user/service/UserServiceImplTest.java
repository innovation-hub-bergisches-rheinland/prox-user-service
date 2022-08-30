package de.innovationhub.prox.userservice.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.shared.avatar.service.AvatarService;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.repository.UserRepository;
import io.quarkus.security.identity.SecurityIdentity;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceImplTest {
  @Mock OrganizationService organizationService;
  @Mock SecurityIdentity securityIdentity;
  @Mock UserRepository userRepository;
  @Mock AvatarService avatarService;
  UserMapper userMapper;

  UserServiceImpl userService;

  AutoCloseable closeable;

  @BeforeEach
  void setup() {
    closeable = MockitoAnnotations.openMocks(this);
    userMapper = UserMapper.INSTANCE;
    userService =
        new UserServiceImpl(
            organizationService, securityIdentity, userRepository, userMapper, avatarService);
  }

  @AfterEach
  void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  void shouldUsePrincipalName() {
    var userId = UUID.randomUUID();
    var mockedPrincipal = mock(Principal.class);
    when(mockedPrincipal.getName()).thenReturn(userId.toString());
    when(securityIdentity.getPrincipal()).thenReturn(mockedPrincipal);

    userService.findOrganizationsOfAuthenticatedUser();

    verify(organizationService).findOrganizationsWhereUserIsMember(eq(userId));
  }

  @Test
  void shouldBuild404ResponseWhenAvatarNotFound() throws IOException {
    when(userRepository.findById(any())).thenReturn(Optional.empty());

    var response = userService.getAvatar(UUID.randomUUID());
    assertThat(response.getStatus()).isEqualTo(404);

    verify(userRepository).findById(any());
  }

  @Test
  void shouldThrow403ExceptionWhenEditingForeignAvatar() throws Exception {
    var userId = UUID.randomUUID();
    var mockedPrincipal = mock(Principal.class);
    when(mockedPrincipal.getName()).thenReturn(userId.toString());
    when(securityIdentity.getPrincipal()).thenReturn(mockedPrincipal);

    var exception =
        assertThrows(
            WebApplicationException.class, () -> userService.setAvatar(UUID.randomUUID(), null));
    assertThat(exception).isNotNull().matches(ex -> ex.getResponse().getStatus() == 403);
  }

  @Test
  void shouldThrow404ExceptionWhenUserNotExisting() throws Exception {
    var userId = UUID.randomUUID();
    var mockedPrincipal = mock(Principal.class);
    when(mockedPrincipal.getName()).thenReturn(userId.toString());
    when(securityIdentity.getPrincipal()).thenReturn(mockedPrincipal);

    var exception =
        assertThrows(WebApplicationException.class, () -> userService.setAvatar(userId, null));
    assertThat(exception).isNotNull().matches(ex -> ex.getResponse().getStatus() == 404);
  }
}
