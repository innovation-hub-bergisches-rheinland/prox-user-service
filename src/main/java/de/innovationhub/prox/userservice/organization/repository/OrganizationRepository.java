package de.innovationhub.prox.userservice.organization.repository;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository {
  Optional<Organization> findById(UUID id);

  List<Organization> findAll();

  List<Organization> findAllWithUserAsMember(UUID id);

  void save(Organization organization);
}
