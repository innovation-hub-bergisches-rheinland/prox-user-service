package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.core.user.entity.ProxUser;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
class OrganizationJpaRepositoryTest {

  @Inject
  OrganizationJpaRepository organizationJpaRepository;

  @Test
  @Disabled("No pre-populated data source introduced")
  void findByIdOptional() {
    // TODO: In order to test this, we should introduce a populated database
    // Given
    var orgId = new OrganizationId(UUID.fromString("00000000-0000-0000-0000-00000000"));

    // When
    var optFound = organizationJpaRepository.findByIdOptional(orgId);

    // Then
    assertThat(optFound).isNotEmpty();
    var foundOrg = optFound.get();

    // TODO: Well isn't this more a mapping test?
    assertThat(foundOrg.getId().id()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-00000000"));
    assertThat(foundOrg.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
    assertThat(foundOrg.getOwner().principal()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-00000000"));
    assertThat(foundOrg.getMembers()).hasSize(1);
  }

  @Test
  void save() {
    // Given
    var owner = new ProxUser(UUID.randomUUID().toString());
    var member = new ProxUser(UUID.randomUUID().toString());
    var org = new Organization(new OrganizationId(UUID.randomUUID()), "Musterfirma GmbH & Co. KG", owner);
    org.addMember(member, new OrganizationMembership(OrganizationRole.MEMBER));

    // WHen
    organizationJpaRepository.save(org);

    // Then

  }
}
