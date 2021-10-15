package de.innovationhub.prox.userservice.application.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.application.service.UserService;
import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationMembershipResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationResponse;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

@SpringBootTest
@AutoConfigureWebTestClient
public class AuthenticatedUserControllerTest {

  @Autowired
  AuthenticatedUserController authenticatedUserController;

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  UserService userService;

  @BeforeEach
  void setup() {}

  @Test
  @WithMockUser
  void given_authenticatedRequestAndMembership_when_GetOrganizationMemberships_should_returnOk() {
    // Given
    var organizationMemberships = Set.of(
      new GetOrganizationMembershipResponse(
        new GetOrganizationResponse(
          UUID.randomUUID(),
          "Musterfirma GmbH & Co. KG"
        ),
        MembershipType.OWNER
      ),
      new GetOrganizationMembershipResponse(
        new GetOrganizationResponse(UUID.randomUUID(), "Musterlabor"),
        MembershipType.MEMBER
      )
    );
    when(this.userService.findMembershipsOfAuthenticatedUser(any()))
      .thenReturn(Flux.fromIterable(organizationMemberships));

    // When
    this.webTestClient.get()
      .uri("/user/memberships/orgs")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBodyList(GetOrganizationMembershipResponse.class)
      .value(response -> {
        assertThat(response)
          .containsExactlyInAnyOrderElementsOf(organizationMemberships);
      });

    verify(this.userService).findMembershipsOfAuthenticatedUser(isNotNull());
  }

  @Test
  @WithMockUser
  void given_authenticatedRequestWithoutMemberships_when_GetOrganizationMemberships_should_returnEmptyList() {
    // Given
    when(this.userService.findMembershipsOfAuthenticatedUser(any()))
      .thenReturn(Flux.empty());

    // When
    this.webTestClient.get()
      .uri("/user/memberships/orgs")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBodyList(GetOrganizationMembershipResponse.class)
      .value(response -> {
        assertThat(response).isEmpty();
      });

    verify(this.userService).findMembershipsOfAuthenticatedUser(isNotNull());
  }
}
