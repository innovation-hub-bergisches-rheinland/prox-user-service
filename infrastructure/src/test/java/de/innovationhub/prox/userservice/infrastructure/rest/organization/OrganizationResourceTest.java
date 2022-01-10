package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.request.FindOrganizationByIdRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.OrganizationResponse;
import de.innovationhub.prox.userservice.application.organization.service.OrganizationService;
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
class OrganizationResourceTest {

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

    verify(organizationService, times(0)).createOrganization(any());
  }

  @Test
  void shouldReturnCreated() {
    var subject = "00000000-0000-0000-0000-000000000000";
    when(organizationService.createOrganization(any())).thenAnswer(invocation -> {
      var request = invocation.getArgument(0, CreateOrganizationRequest.class);
      return new OrganizationResponse(UUID.randomUUID(), request.name(), request.ownerPrincipal());
    });

    given()
        .auth()
          .oauth2(buildAccessToken(subject))
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

    verify(organizationService).createOrganization(any());
  }

  @Test
  void shouldReturnNotFound() {
    var idToFind = UUID.fromString("00000000-0000-0000-0000-000000000001");
    var expectedRequest = new FindOrganizationByIdRequest(UUID.fromString(idToFind.toString()));

    given()
        .accept("application/json")
    .when()
        .get("{id}",idToFind)
    .then()
        .statusCode(404);

    verify(organizationService).findById(eq(expectedRequest));
  }

  @Test
  void shouldReturnOk() {
    when(organizationService.findAll()).thenReturn(Set.of(
        new OrganizationResponse(UUID.fromString("00000000-0000-0000-0000-000000000000"), "Musterfirma GmbH & Co. KG", "00000000-0000-0000-0000-000000000000")
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