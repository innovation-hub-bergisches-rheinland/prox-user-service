package de.innovationhub.prox.userservice.organization.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
class OrganizationRepositoryTest {

  @InjectKafkaCompanion KafkaCompanion kc;

  @Inject ObjectMapper objectMapper;

  @Inject OrganizationPanacheRepository organizationPanacheRepository;

  @Inject OrganizationRepository organizationRepository;

  @Test
  @TestTransaction
  @Disabled("No pre-populated data source introduced")
  void findByIdOptional() {
    // TODO: In order to test this, we should introduce a populated database
    // Given
    var orgId = UUID.fromString("00000000-0000-0000-0000-00000000");

    // When
    var optFound = organizationPanacheRepository.findByIdOptional(orgId);

    // Then
    assertThat(optFound).isNotEmpty();
    var foundOrg = optFound.get();

    // TODO: Well isn't this more a mapping test?
    assertThat(foundOrg.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-00000000"));
    assertThat(foundOrg.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
    assertThat(foundOrg.getMembers()).hasSize(1);
  }

  @Test
  @TestTransaction
  void save() {
    // Given
    var owner = UUID.randomUUID();
    var member = UUID.randomUUID();
    var org =
        new Organization(
            UUID.randomUUID(), "Musterfirma GmbH & Co. KG", new HashMap<>(), null, null);
    org.getMembers().put(owner, new OrganizationMembership(OrganizationRole.ADMIN));
    org.getMembers().put(member, new OrganizationMembership(OrganizationRole.MEMBER));

    // WHen
    organizationPanacheRepository.persist(org);

    // Then

  }

  @Test
  @TestTransaction
  void shouldPublishOnSave() throws Exception {
    // Given
    var owner = UUID.randomUUID();
    var member = UUID.randomUUID();
    var org =
        new Organization(
            UUID.randomUUID(), "Musterfirma GmbH & Co. KG", new HashMap<>(), null, null);
    org.getMembers().put(owner, new OrganizationMembership(OrganizationRole.ADMIN));
    org.getMembers().put(member, new OrganizationMembership(OrganizationRole.MEMBER));

    // WHen
    organizationRepository.save(org);

    // Then
    var records =
        kc.consume(String.class, String.class)
            .fromTopics("entity.organization.organization")
            .awaitRecords(1);
    assertThat(records).hasSize(1);
    var record = records.getFirstRecord();

    // Key assertion for log compaction
    assertThat(record.key()).isEqualTo(org.getId().toString());

    // Value assertion to ensure that the entity state is published
    var publishedOrg = objectMapper.readValue(record.value(), Organization.class);
    assertThat(publishedOrg).isEqualTo(org);
  }

  @Test
  @TestTransaction
  void shouldReturnOrgsWhereUserIsMember() {
    // Given
    var userId = UUID.randomUUID();
    // Org 1 - User is member
    var org1 =
        Organization.builder()
            .id(UUID.randomUUID())
            .name("Test Org 1")
            .members(
                Map.of(
                    userId, OrganizationMembership.builder().role(OrganizationRole.MEMBER).build()))
            .build();
    // Org 2 - User does not have any relation to
    var org2 = Organization.builder().id(UUID.randomUUID()).name("Test Org 2").build();
    this.organizationPanacheRepository.persist(org1, org2);

    // When
    var result = this.organizationRepository.findAllWithUserAsMember(userId);

    // Then
    assertThat(result).containsExactly(org1);
  }

  @Test
  @TestTransaction
  void shouldReturnOrgsWhereUserIsOwner() {
    // Given
    var userId = UUID.randomUUID();
    // Org 1 - User is admin
    var org1 = Organization.builder().id(UUID.randomUUID()).name("Test Org 1").build();
    org1.getMembers().put(userId, new OrganizationMembership(OrganizationRole.ADMIN));
    // Org 2 - User does not have any relation to
    var org2 = Organization.builder().id(UUID.randomUUID()).name("Test Org 2").build();
    this.organizationPanacheRepository.persist(org1, org2);

    var all = this.organizationRepository.findAll();

    // When
    var result = this.organizationRepository.findAllWithUserAsMember(userId);

    // Then
    assertThat(result).containsExactly(org1);
  }
}
