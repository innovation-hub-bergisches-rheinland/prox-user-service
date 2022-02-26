package de.innovationhub.prox.userservice.organization.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import java.util.UUID;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(OrganizationResource.class)
public class OrganizationResourceIntegrationTest {

  KeycloakTestClient keycloakTestClient = new KeycloakTestClient();

  @Inject OrganizationRepository organizationRepository;

  @BeforeEach
  void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Test
  @TestTransaction
  @Order(1)
  void shouldCreateOrganization() {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");

    var orgId =
        RestAssured.given()
            .contentType("application/json")
            .accept("application/json")
            .auth()
            .oauth2(keycloakTestClient.getAccessToken("alice"))
            .body(
                """
                {
                  "name": "ACME Ltd."
                }
                """)
            .when()
            .post()
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is("ACME Ltd."))
            .extract()
            .jsonPath()
            .getUUID("id");

    var org = this.organizationRepository.findById(orgId).get();
    Assertions.assertThat(org.getName()).isEqualTo("ACME Ltd.");
    Assertions.assertThat(org.getOwner()).isEqualTo(aliceId);
  }

  @Test
  @Order(2)
  void shouldCreateMembership() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    this.organizationRepository.save(dummyOrg);
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body(
            """
                {
                  "member": "%s",
                  "role": "MEMBER"
                }
                """
                .formatted(bobId.toString()))
        .when()
        .post("{id}/memberships", orgId.toString())
        .then()
        .body("memberId", is(bobId.toString()))
        .body("name", is("bob"))
        .body("role", is("MEMBER"))
        .statusCode(201);

    var org = this.organizationRepository.findById(orgId).get();
    Assertions.assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers())
        .extractingByKey(bobId)
        .extracting("role")
        .isEqualTo(OrganizationRole.MEMBER);
  }

  @Test
  @Order(3)
  void shouldUpdateMembership() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    dummyOrg.getMembers().put(bobId, new OrganizationMembership(OrganizationRole.MEMBER));
    this.organizationRepository.save(dummyOrg);

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body("""
            {
              "role": "ADMIN"
            }
            """)
        .when()
        .put("{id}/memberships/{memberId}", orgId.toString(), bobId.toString())
        .then()
        .body("memberId", is(bobId.toString()))
        .body("name", is("bob"))
        .body("role", is("ADMIN"))
        .statusCode(200);

    var org = this.organizationRepository.findById(orgId).get();
    Assertions.assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers())
        .extractingByKey(bobId)
        .extracting("role")
        .isEqualTo(OrganizationRole.ADMIN);
  }

  @Test
  @Order(4)
  void shouldRemoveMembership() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    dummyOrg.getMembers().put(bobId, new OrganizationMembership(OrganizationRole.MEMBER));
    this.organizationRepository.save(dummyOrg);

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .when()
        .delete("{id}/memberships/{memberId}", orgId.toString(), bobId.toString())
        .then()
        .statusCode(204);

    var org = this.organizationRepository.findById(orgId).get();
    Assertions.assertThat(org.getMembers()).hasSize(0);
  }

  @Test
  @Order(5)
  void shouldReturn422OnInvalidMemberId() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    this.organizationRepository.save(dummyOrg);
    var randomUser = UUID.randomUUID();

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body(
            """
                {
                  "member": "%s",
                  "role": "MEMBER"
                }
                """
                .formatted(randomUser.toString()))
        .when()
        .post("{id}/memberships", orgId.toString())
        .then()
        .statusCode(422);
  }

  @Test
  @Order(6)
  void shouldGetMemberships() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");
    var julianBradenId = UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    dummyOrg.getMembers().put(bobId, new OrganizationMembership(OrganizationRole.MEMBER));
    dummyOrg.getMembers().put(julianBradenId, new OrganizationMembership(OrganizationRole.ADMIN));
    this.organizationRepository.save(dummyOrg);

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .when()
        .get("{id}/memberships", orgId.toString())
        .then()
        .statusCode(200)
        .body("members", hasSize(3))
        .body("members", everyItem(hasKey("memberId")))
        .body("members", everyItem(hasKey("name")))
        .body("members", everyItem(hasKey("role")))
        .body(
            "members.find { it.memberId == 'ed0b4a07-2612-4571-a9ab-27e13ce752f1' }.name",
            is("bob"))
        .body(
            "members.find { it.memberId == 'ed0b4a07-2612-4571-a9ab-27e13ce752f1' }.role",
            is("MEMBER"))
        .body(
            "members.find { it.memberId == '64788f0d-a954-4898-bfda-7498aae2b271' }.name",
            is("Julian Braden"))
        .body(
            "members.find { it.memberId == '64788f0d-a954-4898-bfda-7498aae2b271' }.role",
            is("ADMIN"))
        .body(
            "members.find { it.memberId == '856ba1b6-ae45-4722-8fa5-212c7f71f10c' }.name",
            is("alice"))
        .body(
            "members.find { it.memberId == '856ba1b6-ae45-4722-8fa5-212c7f71f10c' }.role",
            is("OWNER"));
  }
}
