package de.innovationhub.prox.userservice.domain.organization;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository
  extends CrudRepository<Organization, UUID> {}
