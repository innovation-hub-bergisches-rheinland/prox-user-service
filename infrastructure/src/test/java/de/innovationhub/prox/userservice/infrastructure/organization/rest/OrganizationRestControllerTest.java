package de.innovationhub.prox.userservice.infrastructure.organization.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.domain.membership.repository.OrganizationMembershipRepository;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import de.innovationhub.prox.userservice.domain.membership.vo.OrganizationRole;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.smallrye.jwt.build.Jwt;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(OidcWiremockTestResource.class)
class OrganizationRestControllerTest {

  @Inject
  OrganizationRepository organizationRepository;

  @Inject
  OrganizationMembershipRepository membershipRepository;

  @Inject
  UserRepository userRepository;

  private String getAccessToken(String subject, String userName, Set<String> groups) {
    return Jwt.subject(subject)
        .preferredUserName(userName)
        .groups(groups)
        .issuer("https://server.example.com")
        .audience("https://service.example.com")
        .sign();
  }

  @Test
  @TestTransaction
  void shouldCreateOrganization() {
    // Given
    var principal = UUID.randomUUID().toString();
    var request = new CreateOrganizationRequest("Musterfirma GmbH & Co. KG");

    var response =
      given()
        .contentType("application/json")
        .body(request)
        .auth()
          .oauth2(getAccessToken(principal, "mmustermann", Collections.emptySet()))
      .when()
        .post("/organizations")
      .then()
        .statusCode(201)
        .body("organization.id", is(not(emptyOrNullString())))
        .body("organization.name", is("Musterfirma GmbH & Co. KG"))
      .extract()
        .response();

    var id = UUID.fromString(response.path("organization.id"));

    // Verify that organization is created
    var optOrg = organizationRepository.findByIdOptional(id);
    assertThat(optOrg).isNotEmpty();
    var org = optOrg.get();
    assertThat(org.getId()).isEqualTo(id);
    assertThat(org.getName()).isEqualTo("Musterfirma GmbH & Co. KG");

    // Verify that user is created
    var optUser = userRepository.findByPrincipalOptional(principal);
    assertThat(optUser).isNotEmpty();
    var user = optUser.get();
    assertThat(user.getPrincipal()).isEqualTo(principal);

    // Verify that the user is owner of the org
    var optMembership = membershipRepository.getOrganizationMembershipOptional(org, user);
    assertThat(optMembership).isNotNull();
    var membership = optMembership.get();
    assertThat(membership.getOrganization()).isEqualTo(org);
    assertThat(membership.getUser()).isEqualTo(user);
    assertThat(membership.getRole()).isEqualTo(OrganizationRole.OWNER);

  }
}