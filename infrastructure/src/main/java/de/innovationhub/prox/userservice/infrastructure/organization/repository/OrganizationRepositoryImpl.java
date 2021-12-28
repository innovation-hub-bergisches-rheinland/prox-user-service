package de.innovationhub.prox.userservice.infrastructure.organization.repository;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.infrastructure.organization.mapper.OrganizationJpaMapper;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrganizationRepositoryImpl implements OrganizationRepository {

  private final OrganizationJpaMapper jpaMapper;
  private final OrganizationPanacheRepository organizationPanacheRepository;

  @Inject
  public OrganizationRepositoryImpl(
      OrganizationJpaMapper jpaMapper,
      OrganizationPanacheRepository organizationPanacheRepository) {
    this.jpaMapper = jpaMapper;
    this.organizationPanacheRepository = organizationPanacheRepository;
  }

  @Override
  public Optional<Organization> findByIdOptional(UUID id) {
    return organizationPanacheRepository.findByIdOptional(id)
        .map(jpaMapper::toDomain);
  }

  @Override
  @Transactional
  public void save(Organization organization) {
    var jpaModel = this.jpaMapper.toPersistence(organization);
    organizationPanacheRepository.persist(jpaModel);
  }

}
