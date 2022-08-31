package de.innovationhub.prox.userservice.organization.web;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(OrganizationAdminResource.class)
class OrganizationAdminResourceIntegrationTest {

  KeycloakTestClient keycloakTestClient = new KeycloakTestClient();

  @Test
  void shouldReturnForbidden() {
    // When
    requestAsBob()
        .contentType("application/json")
        .accept("application/json")
        .log()
        .ifValidationFails()
        .when()
        .post("{id}/reconciliation", UUID.randomUUID())
        .then()
        .statusCode(403);
  }

  private RequestSpecification requestAsBob() {
    return RestAssured.given().auth().oauth2(keycloakTestClient.getAccessToken("bob"));
  }
}
