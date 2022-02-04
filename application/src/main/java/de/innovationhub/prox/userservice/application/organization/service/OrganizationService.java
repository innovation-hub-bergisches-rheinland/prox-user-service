package de.innovationhub.prox.userservice.application.organization.service;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.entity.ProxUser;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  public Organization createOrganization(String name, UUID ownerId) {
    // TODO Request Validator
    // TODO: find prox user
    var org = new Organization(new OrganizationId(UUID.randomUUID()), name, new ProxUser(ownerId));
    organizationRepository.save(org);
    return org;
  }

  public Optional<Organization> findById(UUID id) {
    return this.organizationRepository.findByIdOptional(new OrganizationId(id));
  }

  public Set<Organization> findAll() {
    return organizationRepository.findAllOrganizations();
  }
}
