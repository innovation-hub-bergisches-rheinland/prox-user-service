package de.innovationhub.prox.userservice.application.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.application.service.OrganizationService;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationGetDto;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationPostDto;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(classes = { OrganizationController.class })
class OrganizationControllerTest {

  @Autowired
  OrganizationController organizationController;

  WebTestClient webTestClient;

  @MockBean
  OrganizationService organizationService;

  @BeforeEach
  void setup() {
    webTestClient =
      WebTestClient.bindToController(organizationController).build();
  }

  @Test
  void given_organization_when_get_organizationWithId_should_return_organization() {
    var org = new OrganizationGetDto(
      UUID.randomUUID(),
      "Musterfirma GmbH & Co. KG"
    );
    when(organizationService.getOrganizationWithId(org.id()))
      .thenReturn(Mono.just(org));

    // formatter:off
    this.webTestClient.get()
      .uri("/orgs/{id}", org.id().toString())
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isOk()
      .expectBody()
      .jsonPath("$.id")
      .isEqualTo(org.id().toString())
      .jsonPath("$.name")
      .isEqualTo(org.name());
    // formatter:on

    verify(organizationService).getOrganizationWithId(eq(org.id()));
  }

  @Test
  void given_noOrganization_when_get_organizationWithId_should_return_notFound() {
    var orgId = UUID.randomUUID();
    when(organizationService.getOrganizationWithId(eq(orgId)))
      .thenReturn(Mono.empty());

    // formatter:off
    this.webTestClient.get()
      .uri("/orgs/{id}", orgId.toString())
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus()
      .isNotFound();
    // formatter:on

    verify(organizationService).getOrganizationWithId(eq(orgId));
  }

  @Test
  void given_orgDto_when_post_organization_should_return_created() {
    var orgPostDto = new OrganizationPostDto("Musterfirma GmbH & Co. KG");
    var orgGetDto = new OrganizationGetDto(
      UUID.randomUUID(),
      "Musterfirma GmbH & Co. KG"
    );
    when(organizationService.createOrganization(eq(orgPostDto)))
      .thenReturn(Mono.just(orgGetDto));

    // formatter:off
    this.webTestClient.post()
      .uri("/orgs")
      .accept(MediaType.APPLICATION_JSON)
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(orgPostDto)
      .exchange()
      .expectStatus()
      .isCreated();
    // formatter:on

    verify(organizationService).createOrganization(eq(orgPostDto));
  }
}
