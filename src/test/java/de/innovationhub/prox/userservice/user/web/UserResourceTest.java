package de.innovationhub.prox.userservice.user.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.user.dto.UserProfileBriefResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.service.UserService;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
class UserResourceTest {

  @InjectMock UserService userService;

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnUser() {
    // Given
    var userId = UUID.randomUUID();
    var randomUser = new UserSearchResponseDto(userId, "Xavier Tester", Optional.of(new UserProfileBriefResponseDto(
        userId, "Prof. Dr. Xavier Tester", "Quality Assurance")));
    when(userService.findById(eq(userId))).thenReturn(Optional.of(randomUser));

    var response =
        RestAssured.given()
            .accept("application/json")
            .when()
            .get("{id}", userId.toString())
            .then()
            .statusCode(200)
            .extract()
            .jsonPath()
            .getObject(".", UserSearchResponseDto.class);

    Assertions.assertThat(response).isEqualTo(randomUser);
    verify(userService).findById(eq(userId));
  }

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnNotFound() {
    // Given
    when(userService.findById(any())).thenReturn(Optional.empty());

    RestAssured.given()
        .accept("application/json")
        .when()
        .get("{id}", UUID.randomUUID().toString())
        .then()
        .statusCode(404);

    verify(userService).findById(any());
  }

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnSearchResult() {
    // Given
    var searchQuery = "abcdefgh";
    var userId = UUID.randomUUID();
    var randomUser = new UserSearchResponseDto(
        userId, "Xavier Tester", Optional.of(new UserProfileBriefResponseDto(userId, "Prof. Dr. Xavier Tester", "Quality Assurance")));
    var searchResults = List.of(randomUser);
    when(userService.search(eq(searchQuery))).thenReturn(searchResults);

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
            .getList(".", UserSearchResponseDto.class);

    Assertions.assertThat(response).containsExactlyInAnyOrderElementsOf(searchResults);

    verify(userService).search(eq(searchQuery));
  }

  @Test
  @TestSecurity(authorizationEnabled = false)
  void shouldReturnEmptySearchResult() {
    // Given
    var searchQuery = "abcdefgh";
    when(userService.search(eq(searchQuery))).thenReturn(Collections.emptyList());

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
            .getList(".", UserSearchResponseDto.class);

    Assertions.assertThat(response).isEmpty();

    verify(userService).search(eq(searchQuery));
  }
}
