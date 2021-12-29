package de.innovationhub.prox.userservice.application.organization.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.application.organization.mapper.OrganizationMapper;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.service.OrganizationService;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationManagerTest {

  private OrganizationMapper organizationMapper;
  private OrganizationService organizationService;
  private OrganizationManager organizationManager;

  @BeforeEach
  void setUp() {
    this.organizationMapper = OrganizationMapper.INSTANCE;
    this.organizationService = mock(OrganizationService.class);
    this.organizationManager = new OrganizationManager(organizationMapper, organizationService);
  }

  @Test
  void should_create_organization_and_return_successfully() {
    // Given
    var request = new CreateOrganizationRequest("Musterfirma GmbH & Co. KG");
    // TODO: Is this mock necessary? Should we maybe use the real service?
    when(organizationService.create(any(), any())).thenAnswer(invocation ->  new Organization(UUID.randomUUID(), invocation.getArgument(0)));

    // When
    var response = organizationManager.createOrganization(request, "max-mustermann");

    // Then
    assertThat(response).isNotNull();
    assertThat(response.organization()).isNotNull();
    assertThat(response.organization().id()).isNotNull();
    assertThat(response.organization().name()).isEqualTo("Musterfirma GmbH & Co. KG");
    verify(organizationService).create(eq("Musterfirma GmbH & Co. KG"), eq("max-mustermann"));
  }
}