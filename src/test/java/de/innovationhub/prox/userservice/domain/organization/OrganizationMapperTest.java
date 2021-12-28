package de.innovationhub.prox.userservice.domain.organization;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.user.User;
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
}