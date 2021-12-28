package de.innovationhub.prox.userservice.domain.organization;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.application.organization.mapper.OrganizationMapper;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationMapperTest {

  @Test
  void shouldMapOrganizationToGetDto() {
    // Given
    var user = new User(UUID.randomUUID());
    var id = UUID.randomUUID();
    var organization = new Organization(id, "Musterfirma GmbH & Co. KG", Map.of(user, new OrganizationMembership(
        OrganizationRole.OWNER)));

    // When
    var organizationDto = OrganizationMapper.INSTANCE.toGetDto(organization);

    // Then
    assertThat(organizationDto).isNotNull();
    assertThat(organizationDto.id()).isEqualTo(id);
    assertThat(organizationDto.name()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  @Test
  void shouldMapPostDtoToOrganization() {
    // Given
    var userId = UUID.randomUUID();
    var user = new User(userId);
    var organizationPostDto = new CreateOrganizationRequest("Musterfirma GmbH & Co. KG");

    // When
    var organization = OrganizationMapper.INSTANCE.toDomainObject(organizationPostDto, user);

    // Then
    assertThat(organization).isNotNull();
    assertThat(organization.getId()).isNotNull();
    assertThat(organization.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
    assertThat(organization.getMembers()).hasSize(1);
    assertThat(organization.getMembers().get(user)).isNotNull();
    assertThat(organization.getMembers().get(user).role()).isEqualTo(OrganizationRole.OWNER);
  }

  @Test
  void shouldMapOrganizationToOrganizationJpa() {
    // Given
    var user = new User(UUID.randomUUID());
    var id = UUID.randomUUID();
    var organization = new Organization(id, "Musterfirma GmbH & Co. KG", Map.of(user, new OrganizationMembership(OrganizationRole.OWNER)));

    // When
    var jpa = OrganizationMapper.INSTANCE.toPersistenceEntity(organization);

    // Then
    assertThat(jpa).isNotNull();
    assertThat(jpa.getId()).isEqualTo(id);
    assertThat(jpa.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  /*@Test
  void shouldMapOrganizationJpaToOrganization() {
    // Given
    Organization.builder().m

    // When
    var user = UserMapper.INSTANCE.toDomain(userJpa);

    // Then
    assertThat(user).isNotNull();
    assertThat(user.getId()).isEqualTo(id);
  }*/
}