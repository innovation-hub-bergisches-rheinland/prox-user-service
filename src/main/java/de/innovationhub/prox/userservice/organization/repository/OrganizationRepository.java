package de.innovationhub.prox.userservice.organization.repository;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

public interface OrganizationRepository {
  Optional<Organization> findById(UUID id);

  List<Organization> findAll();

  List<Organization> findAllWithUserAsMember(UUID id);

  void save(@Valid Organization organization);
}
