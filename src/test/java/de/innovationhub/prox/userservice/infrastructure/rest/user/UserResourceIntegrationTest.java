package de.innovationhub.prox.userservice.infrastructure.rest.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import de.innovationhub.prox.userservice.user.web.UserResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
public class UserResourceIntegrationTest {

  KeycloakTestClient keycloakTestClient = new KeycloakTestClient();

  @BeforeEach
  void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
  }

  @Test
  void shouldFindByUsername() {
    var searchQuery = "forgisell";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserResponseDto::getId, UserResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByEmail() {
    var searchQuery = "julianbbraden@cuvox.de";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserResponseDto::getId, UserResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByLastName() {
    var searchQuery = "Braden";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserResponseDto::getId, UserResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByFirstName() {
    var searchQuery = "Julian";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserResponseDto::getId, UserResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByFullName() {
    var searchQuery = "Julian Braden";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserResponseDto::getId, UserResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  private List<UserResponseDto> performSearch(String searchQuery) {
    return RestAssured.given()
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .contentType("application/json")
        .accept("application/json")
        .queryParam("q", searchQuery)
        .when()
        .get("search")
        .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList(".", UserResponseDto.class);
  }
}
