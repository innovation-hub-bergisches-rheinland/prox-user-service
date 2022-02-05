package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.organization.repository.OrganizationPanacheRepository;
import io.quarkus.test.junit.QuarkusTest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
class OrganizationPanacheRepositoryTest {

  @Inject
  OrganizationPanacheRepository organizationPanacheRepository;

  @Test
  @Disabled("No pre-populated data source introduced")
  void findByIdOptional() {
    // TODO: In order to test this, we should introduce a populated database
    // Given
    var orgId = UUID.fromString("00000000-0000-0000-0000-00000000");

    // When
    var optFound = organizationPanacheRepository.findByIdOptional(orgId);

    // Then
    Assertions.assertThat(optFound).isNotEmpty();
    var foundOrg = optFound.get();

    // TODO: Well isn't this more a mapping test?
    Assertions.assertThat(foundOrg.getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-00000000"));
    Assertions.assertThat(foundOrg.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
    Assertions.assertThat(foundOrg.getOwner()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-00000000"));
    Assertions.assertThat(foundOrg.getMembers()).hasSize(1);
  }

  @Test
  void save() {
    // Given
    var owner = UUID.randomUUID();
    var member = UUID.randomUUID();
    var org = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG", owner, new HashMap<>());
    org.getMembers().put(member, new OrganizationMembership(OrganizationRole.MEMBER));

    // WHen
    organizationPanacheRepository.persist(org);

    // Then

  }
}
