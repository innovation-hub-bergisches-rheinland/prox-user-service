package de.innovationhub.prox.userservice.domain.organization.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationServiceTest {

  private OrganizationRepository organizationRepository;
  private OrganizationService organizationService;

  @BeforeEach
  void setup() {
    organizationRepository = mock(OrganizationRepository.class);
    organizationService = new OrganizationService(organizationRepository);
  }

  @Test
  void should_create_organization_and_return_successfully() {
    // Given
    var name = "Musterfirma GmbH & Co. KG";

    // When
    var savedOrg = organizationService.create(name);

    // Then
    assertThat(savedOrg).isNotNull();
    verify(organizationRepository).save(any());
  }
}