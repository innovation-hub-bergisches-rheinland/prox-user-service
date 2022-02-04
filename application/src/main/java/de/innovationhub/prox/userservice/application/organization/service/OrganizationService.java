package de.innovationhub.prox.userservice.application.organization.service;

import de.innovationhub.prox.userservice.application.organization.message.OrganizationMessageMapper;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.request.FindOrganizationByIdRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.OrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMessageMapper organizationMessageMapper;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMessageMapper organizationMessageMapper) {
    this.organizationRepository = organizationRepository;
    this.organizationMessageMapper = organizationMessageMapper;
  }

  public OrganizationResponse createOrganization(CreateOrganizationRequest request) {
    // TODO Request Validator
    var org = this.organizationMessageMapper.fromRequest(request);
    organizationRepository.save(org);
    return new OrganizationResponse(org.getId().id(), org.getName(), org.getOwner().id());
  }

  public Optional<OrganizationResponse> findById(FindOrganizationByIdRequest request) {
    return this.organizationRepository.findByIdOptional(new OrganizationId(request.id()))
        .map(organizationMessageMapper::createResponse);
  }

  public Set<OrganizationResponse> findAll() {
    return organizationRepository.findAllOrganizations()
        .stream().map(organizationMessageMapper::createResponse)
        .collect(Collectors.toSet());
  }
}
