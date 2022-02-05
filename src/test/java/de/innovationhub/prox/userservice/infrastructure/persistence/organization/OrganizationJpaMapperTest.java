package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import de.innovationhub.prox.userservice.domain.user.UserId;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrganizationJpaMapperTest {

  private OrganizationJpaMapper mapper = OrganizationJpaMapper.INSTANCE;

  @Test
  void toPersistence() {
    // Given
    var owner = new UserId(UUID.randomUUID());
    var member = new UserId(UUID.randomUUID());
    var org = new Organization(new OrganizationId(UUID.randomUUID()), "Musterfirma GmbH & Co. KG", owner);
    org.addMember(member, new OrganizationMembership(OrganizationRole.MEMBER));

    // When
    var jpa = mapper.toPersistence(org);

    // Then
    Assertions.assertThat(jpa.getId()).isEqualTo(org.getId().id());
    Assertions.assertThat(jpa.getName()).isEqualTo(org.getName());
    Assertions.assertThat(jpa.getOwner()).isEqualTo(owner.id());
    Assertions.assertThat(jpa.getMembers()).hasSize(1);
    Assertions.assertThat(jpa.getMembers()).extracting(
        entry -> entry.getUserId(),
        entry -> entry.getRole()
    ).containsExactlyInAnyOrder(tuple(member.id(), OrganizationRole.MEMBER));
  }

  @Test
  void toDomain() {
    // Given
    var member = OrganizationMembershipJpaEmbeddable.builder()
        .userId(UUID.randomUUID())
        .role(OrganizationRole.MEMBER)
        .build();
    var jpaOrg = OrganizationJpaEntity.builder()
        .id(UUID.randomUUID())
        .name("Musterfirma GmbH & Co. KG")
        .members(Set.of(member))
        .owner(UUID.randomUUID())
        .build();

    // When
    var domain = mapper.toDomain(jpaOrg);

    // Then
    Assertions.assertThat(domain.getName()).isEqualTo(jpaOrg.getName());
    Assertions.assertThat(domain.getId().id()).isEqualTo(jpaOrg.getId());
    Assertions.assertThat(domain.getOwner().id()).isEqualTo(jpaOrg.getOwner());

    Assertions.assertThat(domain.getMembers()).hasSize(1);
    var actualMember = domain.getMembers().get(new UserId(member.getUserId()));
    Assertions.assertThat(actualMember).isNotNull();
    Assertions.assertThat(actualMember.getRole()).isEqualTo(OrganizationRole.MEMBER);
  }
}
