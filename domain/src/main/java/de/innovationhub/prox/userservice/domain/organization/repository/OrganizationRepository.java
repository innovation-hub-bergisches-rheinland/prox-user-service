package de.innovationhub.prox.userservice.domain.organization.repository;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface OrganizationRepository {
  Optional<Organization> findByIdOptional(OrganizationId id);
  Set<Organization> findAllOrganizations();
  void save(Organization organization);
}
