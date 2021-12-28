package de.innovationhub.prox.userservice.domain.organization;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationPostDto;
import de.innovationhub.prox.userservice.domain.user.User;
import de.innovationhub.prox.userservice.domain.user.UserJpa;
import de.innovationhub.prox.userservice.domain.user.UserMapper;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationMapperTest {

  @Test
  void shouldMapOrganizationToGetDto() {
    // Given
    var user = new User(UUID.randomUUID());
    var organization = new Organization("Musterfirma GmbH & Co. KG", user);

    // When
    var organizationDto = OrganizationMapper.INSTANCE.toGetDto(organization);

    // Then
    assertThat(organizationDto).isNotNull();
    assertThat(organizationDto.id()).isNotNull();
    assertThat(organizationDto.name()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  @Test
  void shouldMapPostDtoToOrganization() {
    // Given
    var userId = UUID.randomUUID();
    var user = new User(userId);
    var organizationPostDto = new OrganizationPostDto("Musterfirma GmbH & Co. KG");

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