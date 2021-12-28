package de.innovationhub.prox.userservice.domain.organization.repository;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {
  Optional<Organization> findByIdOptional(UUID id);
  void save(Organization organization);
}
