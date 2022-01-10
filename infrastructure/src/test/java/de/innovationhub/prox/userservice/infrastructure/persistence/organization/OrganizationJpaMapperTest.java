package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.core.user.entity.ProxUser;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationJpaMapperTest {

  private OrganizationJpaMapper mapper = OrganizationJpaMapper.INSTANCE;

  @Test
  void toPersistence() {
    // Given
    var owner = new ProxUser(UUID.randomUUID().toString());
    var member = new ProxUser(UUID.randomUUID().toString());
    var org = new Organization(new OrganizationId(UUID.randomUUID()), "Musterfirma GmbH & Co. KG", owner);
    org.addMember(member, new OrganizationMembership(OrganizationRole.MEMBER));

    // When
    var jpa = mapper.toPersistence(org);

    // Then
    assertThat(jpa.getId()).isEqualTo(org.getId().id());
    assertThat(jpa.getName()).isEqualTo(org.getName());
    assertThat(jpa.getOwner()).isEqualTo(owner.principal());
    assertThat(jpa.getMembers()).hasSize(1);
    assertThat(jpa.getMembers()).extracting(
        entry -> entry.getPrincipal(),
        entry -> entry.getRole()
    ).containsExactlyInAnyOrder(tuple(member.principal(), OrganizationRole.MEMBER));
  }

  @Test
  void toDomain() {
    // Given
    var member = OrganizationMembershipJpaEmbeddable.builder()
        .principal(UUID.randomUUID().toString())
        .role(OrganizationRole.MEMBER)
        .build();
    var jpaOrg = OrganizationJpaEntity.builder()
        .id(UUID.randomUUID())
        .name("Musterfirma GmbH & Co. KG")
        .members(Set.of(member))
        .owner(UUID.randomUUID().toString())
        .build();

    // When
    var domain = mapper.toDomain(jpaOrg);

    // Then
    assertThat(domain.getName()).isEqualTo(jpaOrg.getName());
    assertThat(domain.getId().id()).isEqualTo(jpaOrg.getId());
    assertThat(domain.getOwner().principal()).isEqualTo(jpaOrg.getOwner());

    assertThat(domain.getMembers()).hasSize(1);
    var actualMember = domain.getMembers().get(new ProxUser(member.getPrincipal()));
    assertThat(actualMember).isNotNull();
    assertThat(actualMember.getRole()).isEqualTo(OrganizationRole.MEMBER);
  }
}