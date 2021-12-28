package de.innovationhub.prox.userservice.application.organization.mapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.application.organization.message.dto.OrganizationDTO;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import java.util.UUID;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;

class OrganizationMapperTest {

  @Test
  void toDto() {
    throw new NotImplementedException();
  }

  @Test
  void testToDto() {
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
  void toReadResponse() {
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
}