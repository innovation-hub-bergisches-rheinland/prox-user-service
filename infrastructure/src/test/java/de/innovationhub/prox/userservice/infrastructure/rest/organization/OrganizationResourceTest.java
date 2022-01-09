package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.quarkus.test.security.TestSecurity;
import io.quarkus.test.security.jwt.JwtSecurity;
import io.quarkus.test.security.oidc.Claim;
import io.quarkus.test.security.oidc.OidcSecurity;
import io.quarkus.test.security.oidc.UserInfo;
import io.smallrye.jwt.build.Jwt;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(OrganizationResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class OrganizationResourceTest {

  @Test
  void shouldDenyUnauthenticatedPost() {
    given()
        .contentType("application/json")
        .accept("application/json")
    .when()
        .post()
    .then()
        .statusCode(401);
  }

  @Test
  void shouldReturnCreated() {
    given()
        .auth()
          .oauth2(buildAccessToken("00000000-0000-0000-0000-0000-00000000"))
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
        .body("owner", equalTo("00000000-0000-0000-0000-0000-00000000"));
  }

  private String buildAccessToken(String subject) {
    return Jwt.preferredUserName("foo")
        .issuer("https://server.example.com")
        .audience("https://server.example.com")
        .subject(subject)
        .sign();
  }
}