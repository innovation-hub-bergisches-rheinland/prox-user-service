package de.innovationhub.prox.userservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.application.service.UserService;
import de.innovationhub.prox.userservice.domain.user.User;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.test.StepVerifier;

@SpringBootTest
class UserServiceTest {

  @MockBean
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Test
  void given_unauthenticatedUser_when_getOrCreateAuthenticatedUser_should_throw() {
    StepVerifier
      .create(userService.getOrCreateAuthenticatedUser())
      .expectError()
      .verify();

    verify(userRepository, times(0)).save(any());
  }

  @Test
  @WithMockUser(username = "null")
  void given_authenticatedUserNotUUID_when_getOrCreateAuthenticatedUser_should_throw() {
    StepVerifier
      .create(userService.getOrCreateAuthenticatedUser())
      .expectError()
      .verify();

    verify(userRepository, times(0)).save(any());
  }

  @Test
  @WithMockUser(username = "402da44c-f686-4f65-b27a-ff4866c7aef9")
  void given_authenticatedUserUUID_when_getOrCreateAuthenticatedUser_should_create() {
    var user = new User(
      UUID.fromString("402da44c-f686-4f65-b27a-ff4866c7aef9")
    );
    when(userRepository.save(eq(user))).thenReturn(user);
    when(userRepository.findById(eq(user.getId())))
      .thenReturn(Optional.empty());

    StepVerifier
      .create(userService.getOrCreateAuthenticatedUser())
      .assertNext(next -> {
        assertThat(next).isEqualTo(user);
      })
      .expectComplete()
      .verify();

    verify(userRepository, times(1)).findById(eq(user.getId()));
    verify(userRepository, times(1)).save(eq(user));
  }

  @Test
  @WithMockUser(username = "402da44c-f686-4f65-b27a-ff4866c7aef9")
  void given_authenticatedUserUUID_when_getOrCreateAuthenticatedUser_should_find() {
    var user = new User(
      UUID.fromString("402da44c-f686-4f65-b27a-ff4866c7aef9")
    );
    when(userRepository.findById(eq(user.getId())))
      .thenReturn(Optional.of(user));

    StepVerifier
      .create(userService.getOrCreateAuthenticatedUser())
      .assertNext(next -> {
        assertThat(next).isEqualTo(user);
      })
      .expectComplete()
      .verify();

    verify(userRepository, times(1)).findById(eq(user.getId()));
    verify(userRepository, times(0)).save(eq(user));
  }
}
