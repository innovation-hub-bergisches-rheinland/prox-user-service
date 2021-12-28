package de.innovationhub.prox.userservice.application.organization.manager;

import de.innovationhub.prox.userservice.application.organization.mapper.OrganizationMapper;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.CreateOrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.service.CreateOrganizationService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationManager {
  private final OrganizationMapper organizationMapper;
  private final CreateOrganizationService createOrganizationService;

  @Inject
  public OrganizationManager(
      OrganizationMapper organizationMapper,
      CreateOrganizationService createOrganizationService) {
    this.organizationMapper = organizationMapper;
    this.createOrganizationService = createOrganizationService;
  }

  public CreateOrganizationResponse createOrganization(CreateOrganizationRequest request) {
    var org = createOrganizationService.create(request.name());
    var dto = organizationMapper.toDto(org);
    return organizationMapper.toCreateResponse(dto);
  }
}
