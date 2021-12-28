package de.innovationhub.prox.userservice.infrastructure.organization.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

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
        .body("id", is(not(emptyOrNullString())))
        .body("name", is("Musterfirma GmbH & Co. KG"))
      .extract()
        .response();

    var id = UUID.fromString(response.path("id"));

    var optOrg = organizationRepository.findByIdOptional(id);

    assertThat(optOrg).isNotEmpty();
    var org = optOrg.get();
    assertThat(org.getId()).isEqualTo(id);
    assertThat(org.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
  }
}