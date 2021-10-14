package de.innovationhub.prox.userservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.application.service.UserService;
import de.innovationhub.prox.userservice.domain.organization.Membership;
import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.organization.Organization;
import de.innovationhub.prox.userservice.domain.user.User;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.test.StepVerifier;
import reactor.test.StepVerifier.Step;

@SpringBootTest
class UserServiceTest {

  @MockBean
  UserRepository userRepository;

  @Autowired
  UserService userService;

  @Test
  void given_membership_should_return() {
    // TODO: This piece of code is currently untestable
  }

  @Test
  void given_noMembership_should_empty() {
    // Given
    var userId = UUID.randomUUID();
    var user = new User(userId);
    when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));

    // When
    StepVerifier
      .create(userService.findMembershipsOfUserWithId(userId))
      .expectComplete()
      .verify();
  }
}
