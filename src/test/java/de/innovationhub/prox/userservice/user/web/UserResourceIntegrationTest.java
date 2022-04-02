package de.innovationhub.prox.userservice.user.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.userservice.core.data.FileObject;
import de.innovationhub.prox.userservice.core.data.LocalStackS3Resource;
import de.innovationhub.prox.userservice.core.data.ObjectStoreRepository;
import de.innovationhub.prox.userservice.shared.avatar.entity.Avatar;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.entity.profile.ContactInformation;
import de.innovationhub.prox.userservice.user.entity.profile.Publication;
import de.innovationhub.prox.userservice.user.entity.profile.ResearchSubject;
import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import de.innovationhub.prox.userservice.user.repository.UserProfilePanacheRepository;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

@QuarkusTest
@TestHTTPEndpoint(UserResource.class)
@QuarkusTestResource(LocalStackS3Resource.class)
public class UserResourceIntegrationTest {

  KeycloakTestClient keycloakTestClient = new KeycloakTestClient();

  @Inject UserProfilePanacheRepository userProfilePanacheRepository;
  @Inject TransactionManager tm;
  @Inject ObjectStoreRepository objectStore;
  @Inject S3Client s3Client;

  @ConfigProperty(name = "bucket.name")
  String bucket;

  @BeforeEach
  void setUp() {
    RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

    try {
      s3Client.headBucket(HeadBucketRequest.builder().bucket(bucket).build());
    } catch (NoSuchBucketException e) {
      s3Client.createBucket(CreateBucketRequest.builder().bucket(bucket).build());
    }
  }

  @AfterEach
  @Transactional
  void tearDown() {
    userProfilePanacheRepository.deleteAll();
  }

  @Test
  void shouldFindByUsername() {
    var searchQuery = "forgisell";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::getId, UserSearchResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByEmail() {
    var searchQuery = "julianbbraden@cuvox.de";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::getId, UserSearchResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByLastName() {
    var searchQuery = "Braden";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::getId, UserSearchResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByFirstName() {
    var searchQuery = "Julian";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::getId, UserSearchResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByFullName() {
    var searchQuery = "Julian Braden";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::getId, UserSearchResponseDto::getName)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldCreateUserProfile() {
    String profileRequest =
        """
        {
          "name": "abc",
          "affiliation": "abc",
          "mainSubject": "abc",
          "contactInformation": {
            "room": "abc",
            "consultationHour": "abc",
            "email": "abc",
            "telephone": "abc",
            "homepage": "abc",
            "collegePage": "abc"
          },
          "vita": "Lorem Ipsum",
          "subjects": [
            "a",
            "b",
            "c"
          ],
          "publications": [
            "a",
            "b",
            "c"
          ]
        }
        """;

    requestAsAlice()
        .contentType("application/json")
        .accept("application/json")
        .body(profileRequest)
        .log()
        .ifValidationFails()
        .when()
        .post("{id}/profile", "856ba1b6-ae45-4722-8fa5-212c7f71f10c")
        .then()
        .statusCode(201)
        .body("name", is("abc"))
        .body("affiliation", is("abc"))
        .body("mainSubject", is("abc"))
        .body("contactInformation.room", is("abc"))
        .body("contactInformation.consultationHour", is("abc"))
        .body("contactInformation.email", is("abc"))
        .body("contactInformation.telephone", is("abc"))
        .body("contactInformation.homepage", is("abc"))
        .body("contactInformation.collegePage", is("abc"))
        .body("subjects", containsInAnyOrder("a", "b", "c"))
        .body("publications", containsInAnyOrder("a", "b", "c"))
        .body("vita", is("Lorem Ipsum"));
  }

  @Test
  void shouldGetUserProfile() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var userProfile = dummyUserProfile(aliceId);
    tm.begin();
    userProfilePanacheRepository.persistAndFlush(userProfile);
    tm.commit();

    requestAsAlice()
        .accept("application/json")
        .log()
        .ifValidationFails()
        .when()
        .get("{id}/profile", aliceId.toString())
        .then()
        .statusCode(200)
        .body("name", is("abc"))
        .body("affiliation", is("abc"))
        .body("mainSubject", is("abc"))
        .body("contactInformation.room", is("abc"))
        .body("contactInformation.consultationHour", is("abc"))
        .body("contactInformation.email", is("abc"))
        .body("contactInformation.telephone", is("abc"))
        .body("contactInformation.homepage", is("abc"))
        .body("contactInformation.collegePage", is("abc"))
        .body("subjects", containsInAnyOrder("a", "b", "c"))
        .body("publications", containsInAnyOrder("a", "b", "c"))
        .body("vita", is("Lorem Ipsum"));
  }

  @Test
  void shouldSaveAvatar() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var userProfile = dummyUserProfile(aliceId);
    tm.begin();
    userProfilePanacheRepository.persistAndFlush(userProfile);
    tm.commit();

    requestAsAlice()
        .multiPart("file", getPixelFile())
        .when()
        .post("{id}/profile/avatar", aliceId.toString())
        .then()
        .statusCode(201);
  }

  @Test
  void shouldGetAvatar() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var userProfile = dummyUserProfile(aliceId);
    var fileObj =
        new FileObject("test/img/users/" + aliceId + ".png", "image/png", readPixelImage());
    objectStore.saveObject(fileObj);
    userProfile.setAvatar(new Avatar(fileObj.getKey()));

    tm.begin();
    userProfilePanacheRepository.persistAndFlush(userProfile);
    tm.commit();

    var response =
        requestAsAlice()
            .accept("image/png,image/jpeg")
            .get("{id}/profile/avatar", aliceId.toString())
            .then()
            .statusCode(200)
            .header("Content-Type", "image/png")
            .extract()
            .asByteArray();

    assertThat(Arrays.equals(response, readPixelImage())).isTrue();
  }

  private UserProfile dummyUserProfile(UUID id) {
    return new UserProfile(
        id,
        "abc",
        "abc",
        "abc",
        new ContactInformation("abc", "abc", "abc", "abc", "abc", "abc"),
        Stream.of("a", "b", "c").map(ResearchSubject::new).toList(),
        Stream.of("a", "b", "c").map(Publication::new).toList(),
        "Lorem Ipsum",
        null);
  }

  private RequestSpecification requestAsAlice() {
    return RestAssured.given().auth().oauth2(keycloakTestClient.getAccessToken("alice"));
  }

  private List<UserSearchResponseDto> performSearch(String searchQuery) {
    return requestAsAlice()
        .contentType("application/json")
        .accept("application/json")
        .queryParam("q", searchQuery)
        .when()
        .get("search")
        .then()
        .statusCode(200)
        .extract()
        .jsonPath()
        .getList(".", UserSearchResponseDto.class);
  }

  private File getPixelFile() throws Exception {
    return new File(getClass().getClassLoader().getResource("img/pixel-image.png").toURI());
  }

  private byte[] readPixelImage() throws IOException {
    return getClass().getClassLoader().getResourceAsStream("img/pixel-image.png").readAllBytes();
  }
}
