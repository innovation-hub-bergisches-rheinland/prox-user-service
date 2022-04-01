package de.innovationhub.prox.userservice.user.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import de.innovationhub.prox.userservice.user.service.UserIdentityService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
class UserResourceTest {

  @InjectMock UserIdentityService userIdentityService;

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnUser() {
    // Given
    var easyRandom = new EasyRandom();
    var userId = UUID.randomUUID();
    var randomUser = easyRandom.nextObject(UserResponseDto.class);
    when(userIdentityService.findById(eq(userId))).thenReturn(Optional.of(randomUser));

    var response =
        RestAssured.given()
            .accept("application/json")
            .when()
            .get("{id}", userId.toString())
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getObject(".", UserResponseDto.class);

    Assertions.assertThat(response).isEqualTo(randomUser);
    verify(userIdentityService).findById(eq(userId));
  }

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnNotFound() {
    // Given
    when(userIdentityService.findById(any())).thenReturn(Optional.empty());

    RestAssured.given()
        .accept("application/json")
        .when()
        .get("{id}", UUID.randomUUID().toString())
        .then()
        .statusCode(404);

    verify(userIdentityService).findById(any());
  }

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnSearchResult() {
    // Given
    var searchQuery = "abcdefgh";
    var easyRandom = new EasyRandom();
    var searchResults = easyRandom.objects(UserResponseDto.class, 5).collect(Collectors.toList());
    when(userIdentityService.search(eq(searchQuery))).thenReturn(searchResults);

    var response =
        RestAssured.given()
            .accept("application/json")
            .queryParam("q", searchQuery)
            .when()
            .get("search")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList(".", UserResponseDto.class);

    Assertions.assertThat(response).containsExactlyInAnyOrderElementsOf(searchResults);

    verify(userIdentityService).search(eq(searchQuery));
  }

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnEmptySearchResult() {
    // Given
    var searchQuery = "abcdefgh";
    when(userIdentityService.search(eq(searchQuery))).thenReturn(Collections.emptyList());

    var response =
        RestAssured.given()
            .accept("application/json")
            .queryParam("q", searchQuery)
            .when()
            .get("search")
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getList(".", UserResponseDto.class);

    Assertions.assertThat(response).isEmpty();

    verify(userIdentityService).search(eq(searchQuery));
  }
}
