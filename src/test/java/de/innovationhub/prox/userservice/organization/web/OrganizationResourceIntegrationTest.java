package de.innovationhub.prox.userservice.organization.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.organization.entity.profile.Branch;
import de.innovationhub.prox.userservice.organization.entity.profile.OrganizationProfile;
import de.innovationhub.prox.userservice.organization.entity.profile.Quarter;
import de.innovationhub.prox.userservice.organization.entity.profile.SocialMedia;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import java.util.Set;
import java.util.UUID;
import javax.inject.Inject;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
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

  @Test
  @Order(7)
  void shouldCreateProfile() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    this.organizationRepository.save(dummyOrg);

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .auth()
        .oauth2(keycloakTestClient.getAccessToken("alice"))
        .body(
            """
                {
                  "foundingDate": "14.03.2022",
                  "numberOfEmployees": "2022",
                  "homepage": "https://example.org",
                  "contactEmail": "example@example.org",
                  "vita": "Lorem Ipsum",
                  "headquarter": "Gummersbach",
                  "quarters": [
                    "Abu Dhabi"
                  ],
                  "branches": [
                    "Automotive",
                    "Quality Assurance"
                  ],
                  "socialMedia": {
                    "facebookHandle": "acmeLtd",
                    "twitterHandle": "acmeLtd",
                    "instagramHandle": "acmeLtd",
                    "xingHandle": "acmeLtd",
                    "linkedInHandle": "acmeLtd"
                  }
                }
            """)
        .when()
        .put("{id}/profile", orgId.toString())
        .then()
        .body("foundingDate", is("14.03.2022"))
        .body("numberOfEmployees", is("2022"))
        .body("homepage", is("https://example.org"))
        .body("contactEmail", is("example@example.org"))
        .body("vita", is("Lorem Ipsum"))
        .body("headquarter", is("Gummersbach"))
        .body("quarters", containsInAnyOrder("Abu Dhabi"))
        .body("branches", containsInAnyOrder("Automotive", "Quality Assurance"))
        .body("socialMedia.facebookHandle", is("acmeLtd"))
        .body("socialMedia.twitterHandle", is("acmeLtd"))
        .body("socialMedia.instagramHandle", is("acmeLtd"))
        .body("socialMedia.xingHandle", is("acmeLtd"))
        .body("socialMedia.linkedInHandle", is("acmeLtd"))
        .statusCode(200);

    var profile = this.organizationRepository.findById(orgId).get().getProfile();
    assertThat(profile).isNotNull();

    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(profile.getFoundingDate()).isEqualTo("14.03.2022");
    softly.assertThat(profile.getNumberOfEmployees()).isEqualTo("2022");
    softly.assertThat(profile.getHomepage()).isEqualTo("https://example.org");
    softly.assertThat(profile.getContactEmail()).isEqualTo("example@example.org");
    softly.assertThat(profile.getVita()).isEqualTo("Lorem Ipsum");
    softly.assertThat(profile.getHeadquarter().getLocation()).isEqualTo("Gummersbach");
    softly
        .assertThat(profile.getQuarters())
        .extracting("location")
        .containsExactlyInAnyOrder("Abu Dhabi");
    softly
        .assertThat(profile.getBranches())
        .extracting("name")
        .containsExactlyInAnyOrder("Automotive", "Quality Assurance");
    softly.assertThat(profile.getSocialMedia().getFacebookHandle()).isEqualTo("acmeLtd");
    softly.assertThat(profile.getSocialMedia().getTwitterHandle()).isEqualTo("acmeLtd");
    softly.assertThat(profile.getSocialMedia().getInstagramHandle()).isEqualTo("acmeLtd");
    softly.assertThat(profile.getSocialMedia().getXingHandle()).isEqualTo("acmeLtd");
    softly.assertThat(profile.getSocialMedia().getLinkedInHandle()).isEqualTo("acmeLtd");
    softly.assertAll();
  }

  @Test
  @Order(8)
  void shouldGetProfile() {
    // Given
    var aliceId = UUID.fromString("856ba1b6-ae45-4722-8fa5-212c7f71f10c");
    var orgId = UUID.randomUUID();
    var dummyOrg = Organization.builder().id(orgId).name("ACME Ltd.").owner(aliceId).build();
    dummyOrg.setProfile(
        new OrganizationProfile(
            "14.03.2022",
            "2022",
            "https://example.org",
            "example@example.org",
            "Lorem Ipsum",
            new Quarter("Gummersbach"),
            Set.of(new Quarter("Abu Dhabi")),
            Set.of(new Branch("Automotive"), new Branch("Quality Assurance")),
            new SocialMedia("acmeLtd", "acmeLtd", "acmeLtd", "acmeLtd", "acmeLtd")));
    this.organizationRepository.save(dummyOrg);

    RestAssured.given()
        .contentType("application/json")
        .accept("application/json")
        .when()
        .get("{id}/profile", orgId.toString())
        .then()
        .body("foundingDate", is("14.03.2022"))
        .body("numberOfEmployees", is("2022"))
        .body("homepage", is("https://example.org"))
        .body("contactEmail", is("example@example.org"))
        .body("vita", is("Lorem Ipsum"))
        .body("headquarter", is("Gummersbach"))
        .body("quarters", containsInAnyOrder("Abu Dhabi"))
        .body("branches", containsInAnyOrder("Automotive", "Quality Assurance"))
        .body("socialMedia.facebookHandle", is("acmeLtd"))
        .body("socialMedia.twitterHandle", is("acmeLtd"))
        .body("socialMedia.instagramHandle", is("acmeLtd"))
        .body("socialMedia.xingHandle", is("acmeLtd"))
        .body("socialMedia.linkedInHandle", is("acmeLtd"))
        .statusCode(200);
  }
}
