package de.innovationhub.prox.userservice.application.organization.manager;

import de.innovationhub.prox.userservice.application.organization.mapper.OrganizationMapper;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.CreateOrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.service.OrganizationService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationManager {
  private final OrganizationMapper organizationMapper;
  private final OrganizationService organizationService;

  @Inject
  public OrganizationManager(
      OrganizationMapper organizationMapper,
      OrganizationService createOrganizationService) {
    this.organizationMapper = organizationMapper;
    this.organizationService = createOrganizationService;
  }

  public CreateOrganizationResponse createOrganization(CreateOrganizationRequest request) {
    var org = organizationService.create(request.name());
    var dto = organizationMapper.toDto(org);
    return organizationMapper.toCreateResponse(dto);
  }
}
