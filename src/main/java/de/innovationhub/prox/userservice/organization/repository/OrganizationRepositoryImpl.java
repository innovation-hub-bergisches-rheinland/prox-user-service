package de.innovationhub.prox.userservice.organization.repository;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrganizationRepositoryImpl implements OrganizationRepository {
  private final OrganizationPanacheRepository organizationPanacheRepository;

  @Inject
  public OrganizationRepositoryImpl(OrganizationPanacheRepository organizationPanacheRepository) {
    this.organizationPanacheRepository = organizationPanacheRepository;
  }

  @Override
  @Transactional
  public Optional<Organization> findById(UUID id) {
    return organizationPanacheRepository.findByIdOptional(id);
  }

  @Override
  @Transactional
  public List<Organization> findAll() {
    return organizationPanacheRepository.findAll().list();
  }

  @Override
  @Transactional
  public void save(Organization organization) {
    this.organizationPanacheRepository.persist(organization);
  }
}
