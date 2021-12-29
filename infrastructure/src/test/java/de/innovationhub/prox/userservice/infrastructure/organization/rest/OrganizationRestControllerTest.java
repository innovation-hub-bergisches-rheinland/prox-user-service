package de.innovationhub.prox.userservice.infrastructure.organization.rest;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
class OrganizationRestControllerTest {

  @Inject
  OrganizationRepository organizationRepository;

  @Test
  @TestTransaction
  void shouldCreateOrganization() {
    var request = new CreateOrganizationRequest("Musterfirma GmbH & Co. KG");

    var response =
      given()
        .contentType("application/json")
        .body(request)
      .when()
        .post("/organizations")
      .then()
        .statusCode(201)
        .body("organization.id", is(not(emptyOrNullString())))
        .body("organization.name", is("Musterfirma GmbH & Co. KG"))
      .extract()
        .response();

    var id = UUID.fromString(response.path("organization.id"));

    var optOrg = organizationRepository.findByIdOptional(id);

    assertThat(optOrg).isNotEmpty();
    var org = optOrg.get();
    assertThat(org.getId()).isEqualTo(id);
    assertThat(org.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
  }
}