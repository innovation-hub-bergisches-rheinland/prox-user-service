package de.innovationhub.prox.userservice.domain.organization.service;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CreateOrganizationService {
  private final OrganizationRepository organizationRepository;

  @Inject
  public CreateOrganizationService(
      OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  public Organization create(String name) {
    var org = new Organization(UUID.randomUUID(), name);
    this.organizationRepository.save(org);
    return org;
  }
}
