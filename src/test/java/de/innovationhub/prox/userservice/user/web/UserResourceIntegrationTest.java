package de.innovationhub.prox.userservice.user.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import de.innovationhub.prox.userservice.core.data.FileObject;
import de.innovationhub.prox.userservice.core.data.LocalStackS3Resource;
import de.innovationhub.prox.userservice.core.data.ObjectStoreRepository;
import de.innovationhub.prox.userservice.shared.avatar.entity.Avatar;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.entity.profile.ContactInformation;
import de.innovationhub.prox.userservice.user.entity.profile.Publication;
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
import org.assertj.core.api.SoftAssertions;
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
  void tearDown() throws Exception {
    tm.begin();
    userProfilePanacheRepository.findAll().stream()
        .forEach(up -> userProfilePanacheRepository.delete(up));
    tm.commit();
  }

  @Test
  void shouldFindByUsername() {
    var searchQuery = "forgisell";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByEmail() {
    var searchQuery = "julianbbraden@cuvox.de";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByLastName() {
    var searchQuery = "Braden";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByFirstName() {
    var searchQuery = "Julian";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByFullName() {
    var searchQuery = "Julian Braden";

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271"), "Julian Braden"));
  }

  @Test
  void shouldFindByProfileName() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var searchQuery = "Prof. Dr. Alice";
    var userProfile = dummyUserProfile(aliceId, "Prof. Dr. Alice");

    var searchResult = findByProfileName(userProfile, searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(aliceId, "alice"));
  }

  @Test
  void shouldFindByProfileNameCaseInsensitive() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var searchQuery = "prof. dr. alice";
    var userProfile = dummyUserProfile(aliceId, "Prof. Dr. Alice");

    var searchResult = findByProfileName(userProfile, searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(aliceId, "alice"));
  }

  @Test
  void shouldFindByProfileNamePartial() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var searchQuery = "Dr. Alice";
    var userProfile = dummyUserProfile(aliceId, "Prof. Dr. Alice");

    var searchResult = findByProfileName(userProfile, searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .contains(tuple(aliceId, "alice"));
  }

  private List<UserSearchResponseDto> findByProfileName(UserProfile userProfile, String searchQuery)
      throws Exception {
    tm.begin();
    userProfilePanacheRepository.persistAndFlush(userProfile);
    tm.commit();

    return performSearch(searchQuery);
  }

  @Test
  void shouldNotFindDuplicatesIfProfileNameAndUsernameMatch() throws Exception {
    var julianId = UUID.fromString("64788f0d-a954-4898-bfda-7498aae2b271");
    var searchQuery = "Julian Braden";
    var userProfile = dummyUserProfile(julianId, "Julian Braden");

    tm.begin();
    userProfilePanacheRepository.persistAndFlush(userProfile);
    tm.commit();

    var searchResult = performSearch(searchQuery);

    assertThat(searchResult)
        .extracting(UserSearchResponseDto::id, UserSearchResponseDto::name)
        .hasSize(1);
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
        .body("publications", containsInAnyOrder("a", "b", "c"))
        .body("vita", is("Lorem Ipsum"));
  }

  @Test
  void shouldUpdateUserProfile() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var userProfile = dummyUserProfile(aliceId);
    tm.begin();
    userProfilePanacheRepository.persistAndFlush(userProfile);
    tm.commit();

    String profileRequest =
        """
        {
          "name": "cba",
          "affiliation": "cba",
          "mainSubject": "cba",
          "contactInformation": {
            "room": "cba",
            "consultationHour": "cba",
            "email": "cba",
            "telephone": "cba",
            "homepage": "cba",
            "collegePage": "cba"
          },
          "vita": "Ipsum Lorem",
          "subjects": [
            "ab",
            "bc",
            "cd"
          ],
          "publications": [
            "ab",
            "bc",
            "cd"
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
        .post("{id}/profile", aliceId.toString())
        .then()
        .statusCode(201)
        .body("name", is("cba"))
        .body("affiliation", is("cba"))
        .body("mainSubject", is("cba"))
        .body("contactInformation.room", is("cba"))
        .body("contactInformation.consultationHour", is("cba"))
        .body("contactInformation.email", is("cba"))
        .body("contactInformation.telephone", is("cba"))
        .body("contactInformation.homepage", is("cba"))
        .body("contactInformation.collegePage", is("cba"))
        .body("publications", containsInAnyOrder("ab", "bc", "cd"))
        .body("vita", is("Ipsum Lorem"));

    SoftAssertions softly = new SoftAssertions();
    var profile = userProfilePanacheRepository.findById(aliceId);

    softly.assertThat(profile.getName()).isEqualTo("cba");
    softly.assertThat(profile.getAffiliation()).isEqualTo("cba");
    softly
        .assertThat(profile.getContactInformation())
        .extracting("room", "consultationHour", "email", "telephone", "homepage", "collegePage")
        .containsExactly("cba", "cba", "cba", "cba", "cba", "cba");
    softly.assertThat(profile.getMainSubject()).isEqualTo("cba");
    softly.assertThat(profile.getVita()).isEqualTo("Ipsum Lorem");
    softly
        .assertThat(profile.getPublications())
        .extracting("publication")
        .containsExactlyInAnyOrder("ab", "bc", "cd");

    softly.assertAll();
  }

  @Test
  void shouldGetAllUserProfiles() throws Exception {
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    UUID bobId = UUID.fromString("ed0b4a07-2612-4571-a9ab-27e13ce752f1");
    var aliceUserProfile = dummyUserProfile(aliceId, "Alice");
    var bobUserProfile = dummyUserProfile(bobId, "Bob");
    tm.begin();
    userProfilePanacheRepository.persistAndFlush(aliceUserProfile);
    userProfilePanacheRepository.persistAndFlush(bobUserProfile);
    tm.commit();

    requestAsAlice()
        .accept("application/json")
        .log()
        .ifValidationFails()
        .when()
        .get("profiles")
        .then()
        .statusCode(200)
        .body("profiles", hasSize(2))
        .body("profiles[0].id", is("856ba1b6-ae45-4722-8fa5-212c7f71f10c"))
        .body("profiles[0].name", is("Alice"))
        .body("profiles[0].mainSubject", is("abc"))
        .body("profiles[1].id", is("ed0b4a07-2612-4571-a9ab-27e13ce752f1"))
        .body("profiles[1].name", is("Bob"))
        .body("profiles[1].mainSubject", is("abc"));
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
    return dummyUserProfile(id, "abc");
  }

  private UserProfile dummyUserProfile(UUID id, String name) {
    return new UserProfile(
        id,
        name,
        "abc",
        "abc",
        new ContactInformation("abc", "abc", "abc", "abc", "abc", "abc"),
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
