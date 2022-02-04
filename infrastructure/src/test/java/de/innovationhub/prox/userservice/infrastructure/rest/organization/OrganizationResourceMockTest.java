package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.application.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.user.entity.ProxUser;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.smallrye.jwt.build.Jwt;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(OrganizationResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class OrganizationResourceMockTest {

  @InjectMock
  OrganizationService organizationService;

  @Test
  void shouldDenyUnauthenticatedPost() {
    given()
        .contentType("application/json")
        .accept("application/json")
    .when()
        .post()
    .then()
        .statusCode(401);

    verify(organizationService, times(0)).createOrganization(any(), any());
  }

  @Test
  void shouldReturnCreated() {
    var subject = UUID.fromString("00000000-0000-0000-0000-000000000000");
    when(organizationService.createOrganization(any(), eq(subject))).thenReturn(new Organization(new OrganizationId(UUID.randomUUID()), "Musterfirma GmbH & Co. KG", new ProxUser(subject)));

    given()
        .auth()
          .oauth2(buildAccessToken(subject.toString()))
        .contentType("application/json")
        .accept("application/json")
        .body("""
            {
              "name": "Musterfirma GmbH & Co. KG"
            }
            """)
    .when()
        .post()
    .then()
        .statusCode(201)
        .body("name", equalTo("Musterfirma GmbH & Co. KG"))
        .body("id", not(emptyOrNullString()))
        .body("owner", equalTo("00000000-0000-0000-0000-000000000000"));

    verify(organizationService).createOrganization(any(), eq(subject));
  }

  @Test
  void shouldReturnNotFound() {
    var idToFind = UUID.fromString("00000000-0000-0000-0000-000000000001");

    given()
        .accept("application/json")
    .when()
        .get("{id}",idToFind)
    .then()
        .statusCode(404);

    verify(organizationService).findById(eq(idToFind));
  }

  @Test
  void shouldReturnOk() {
    when(organizationService.findAll()).thenReturn(Set.of(
        new Organization(new OrganizationId(UUID.fromString("00000000-0000-0000-0000-000000000000")), "Musterfirma GmbH & Co. KG", new ProxUser(UUID.fromString("00000000-0000-0000-0000-000000000000")))
    ));

    given()
        .accept("application/json")
    .when()
        .get()
    .then()
        .statusCode(200)
        .body("organizations", hasSize(1))
        .body("organizations[0].id", equalTo("00000000-0000-0000-0000-000000000000"))
        .body("organizations[0].name", equalTo("Musterfirma GmbH & Co. KG"))
        .body("organizations[0].owner", equalTo("00000000-0000-0000-0000-000000000000"));

    verify(organizationService).findAll();
  }

  private String buildAccessToken(String subject) {
    return Jwt.preferredUserName("foo")
        .issuer("https://server.example.com")
        .audience("https://server.example.com")
        .subject(subject)
        .sign();
  }
}
