package de.innovationhub.prox.userservice.application.organization.mapper;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.application.organization.message.dto.OrganizationDTO;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class OrganizationMapperTest {

  @Test
  void should_map_Organization_to_OrganizationDTO() {
    // Given
    var id = UUID.randomUUID();
    var org = new Organization(id, "Musterfirma GmbH & Co. KG");

    // When
    var dto = OrganizationMapper.INSTANCE.toDto(org);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.id()).isEqualTo(id);
    assertThat(dto.name()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  @Test
  void should_map_CreateOrganizationRequest_to_OrganizationDTO() {
    // Given
    var request = new CreateOrganizationRequest("Musterfirma GmbH & Co. KG");

    // When
    var dto = OrganizationMapper.INSTANCE.toDto(request);

    // Then
    assertThat(dto).isNotNull();
    assertThat(dto.id()).isNotNull();
    assertThat(dto.name()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  @Test
  void should_map_OrganizationDTO_to_ReadOrganizationResponse() {
    // Given
    var id = UUID.randomUUID();
    var dto = new OrganizationDTO(id, "Musterfirma GmbH & Co. KG");

    // When
    var response = OrganizationMapper.INSTANCE.toReadResponse(dto);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(id);
    assertThat(response.name()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  @Test
  void should_map_OrganizationDTO_to_CreateOrganizationResponse() {
    // Given
    var id = UUID.randomUUID();
    var dto = new OrganizationDTO(id, "Musterfirma GmbH & Co. KG");

    // When
    var response = OrganizationMapper.INSTANCE.toCreateResponse(dto);

    // Then
    assertThat(response).isNotNull();
    assertThat(response.id()).isEqualTo(id);
    assertThat(response.name()).isEqualTo("Musterfirma GmbH & Co. KG");
  }
}