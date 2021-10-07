package de.innovationhub.prox.userservice.application.web;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
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
public class OrganizationControllerIntegrationTest {

  @Autowired
  OrganizationRepository organizationRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  WebTestClient webTestClient;

  @BeforeEach
  void setup() {}

  @Test
  void given_JSONandToken_when_PostOrganization_should_createOrganizationAndUser() {
    var orgJson =
      """
        {
          "name": "Musterfirma GmbH & Co. KG"
        }
        """;
    var userId = UUID.randomUUID();

    webTestClient
      .mutateWith(
        mockJwt()
          .jwt(jwt ->
            jwt
              .claim("iss", "https://login.archi-lab.io/auth/realms/archilab")
              .claim("sub", userId)
          )
      )
      .post()
      .uri("/orgs")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(orgJson)
      .exchange()
      .expectStatus()
      .isCreated()
      .expectBody()
      .jsonPath("$.id")
      .isNotEmpty()
      .jsonPath("$.id")
      .value(id -> {
        assertThat(
          this.organizationRepository.existsById(UUID.fromString((String) id))
        )
          .isTrue();
      })
      .jsonPath("$.name")
      .isEqualTo("Musterfirma GmbH & Co. KG");

    assertThat(this.userRepository.existsById(userId)).isTrue();
  }

  @Test
  @Transactional(TxType.NOT_SUPPORTED) // TODO
  void given_JSONandTokenandUser_when_PostOrganization_should_createOrganization() {
    var orgJson =
      """
        {
          "name": "Musterfirma GmbH & Co. KG"
        }
        """;
    var userId = UUID.randomUUID();
    var user = new User(userId);
    this.userRepository.save(user);

    webTestClient
      .mutateWith(
        mockJwt()
          .jwt(jwt ->
            jwt
              .claim("iss", "https://login.archi-lab.io/auth/realms/archilab")
              .claim("sub", userId)
          )
      )
      .post()
      .uri("/orgs")
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(orgJson)
      .exchange()
      .expectStatus()
      .isCreated()
      .expectBody()
      .jsonPath("$.id")
      .isNotEmpty()
      .jsonPath("$.id")
      .value(id -> {
        assertThat(
          this.organizationRepository.existsById(UUID.fromString((String) id))
        )
          .isTrue();
      })
      .jsonPath("$.name")
      .isEqualTo("Musterfirma GmbH & Co. KG");

    assertThat(this.userRepository.existsById(userId)).isTrue();
  }
}
