package de.innovationhub.prox.userservice.application.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.organization.Organization;
import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationMembershipResponse;
import de.innovationhub.prox.userservice.domain.user.User;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@Transactional
public class AuthenticatedUserControllerIntegrationTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  WebTestClient webTestClient;

  @BeforeEach
  void setup() {}

  @Test
  @Transactional(TxType.NOT_SUPPORTED) // TODO
  void given_memberShips_should_return_memberships() {
    // Given
    var user = new User(UUID.randomUUID());
    var user2 = new User(UUID.randomUUID());
    var organization = new Organization("Musterfirma GmbH & Co. KG", user);
    var organization2 = new Organization("Testlabor", user2);
    organization2.addMember(user);
    this.userRepository.save(user);
    this.userRepository.save(user2);
    this.organizationRepository.save(organization);
    this.organizationRepository.save(organization2);

    // When
    webTestClient
      .mutateWith(
        mockJwt()
          .jwt(jwt ->
            jwt
              .claim("iss", "https://login.archi-lab.io/auth/realms/archilab")
              .claim("sub", user.getId())
          )
      )
      .get()
      .uri("/user/memberships/orgs")
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBodyList(GetOrganizationMembershipResponse.class)
      .value(members -> {
        assertThat(members)
          .extracting("organization.id", "organization.name", "type")
          .containsExactlyInAnyOrder(
            tuple(
              organization.getId(),
              organization.getName(),
              MembershipType.OWNER
            ),
            tuple(
              organization2.getId(),
              organization2.getName(),
              MembershipType.MEMBER
            )
          );
      });
  }
}
