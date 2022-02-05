package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import de.innovationhub.prox.userservice.domain.core.user.UserId;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import java.util.UUID;
import javax.inject.Inject;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(OrganizationResource.class)
public class OrganizationResourceIntegrationTest {
  KeycloakTestClient keycloakTestClient = new KeycloakTestClient();

  @Inject
  OrganizationRepository organizationRepository;

  @Test
  @Order(1)
  void shouldCreateOrganization() {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");

    var orgId = given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body("""
            {
              "name": "ACME Ltd."
            }
            """)
    .when()
        .post()
    .then()
        .statusCode(201)
        .extract()
        .jsonPath().getUUID("id");

    var org = this.organizationRepository.findByIdOptional(new OrganizationId(orgId)).get();
    assertThat(org.getName()).isEqualTo("ACME Ltd.");
    assertThat(org.getOwner().id()).isEqualTo(aliceId);
  }

  @Test
  @Order(2)
  void shouldCreateMembership() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var orgId = UUID.randomUUID();
    var dummyOrg = new Organization(new OrganizationId(orgId), "ACME Ltd.", new UserId(aliceId));
    this.organizationRepository.save(dummyOrg);
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");

    given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body("""
            {
              "userId": "%s",
              "role": "MEMBER"
            }
            """.formatted(bobId.toString()))
        .when()
    .post("{id}/memberships", orgId.toString())
        .then()
        .statusCode(201);

    var org = this.organizationRepository.findByIdOptional(new OrganizationId(orgId)).get();
    assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers().get(new UserId(bobId))).matches(membership -> membership.getRole().equals(
        OrganizationRole.MEMBER));
  }

  @Test
  @Order(3)
  void shouldUpdateMembership() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");
    var orgId = UUID.randomUUID();
    var dummyOrg = new Organization(new OrganizationId(orgId), "ACME Ltd.", new UserId(aliceId));
    dummyOrg.addMember(new UserId(bobId), new OrganizationMembership(OrganizationRole.MEMBER));
    this.organizationRepository.save(dummyOrg);


    given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body("""
            {
              "role": "ADMIN"
            }
            """.formatted(bobId.toString()))
        .when()
        .put("{id}/memberships/{memberId}", orgId.toString(), bobId.toString())
        .then()
        .statusCode(200);

    var org = this.organizationRepository.findByIdOptional(new OrganizationId(orgId)).get();
    assertThat(org.getMembers()).hasSize(1);
    assertThat(org.getMembers().get(new UserId(bobId))).matches(membership -> membership.getRole().equals(
        OrganizationRole.ADMIN));
  }

  @Test
  @Order(4)
  void shouldRemoveMembership() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");
    var orgId = UUID.randomUUID();
    var dummyOrg = new Organization(new OrganizationId(orgId), "ACME Ltd.", new UserId(aliceId));
    dummyOrg.addMember(new UserId(bobId), new OrganizationMembership(OrganizationRole.MEMBER));
    this.organizationRepository.save(dummyOrg);

    given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .when()
        .delete("{id}/memberships/{memberId}", orgId.toString(), bobId.toString())
        .then()
        .statusCode(204);

    var org = this.organizationRepository.findByIdOptional(new OrganizationId(orgId)).get();
    assertThat(org.getMembers()).hasSize(0);
  }
}
