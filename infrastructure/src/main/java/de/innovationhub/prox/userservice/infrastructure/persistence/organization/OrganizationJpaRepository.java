package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrganizationJpaRepository implements PanacheRepositoryBase<OrganizationJpaEntity, UUID>,
    OrganizationRepository {
  private final OrganizationJpaMapper jpaMapper;

  @Inject
  public OrganizationJpaRepository(
      OrganizationJpaMapper jpaMapper) {
    this.jpaMapper = jpaMapper;
  }

  @Override
  public Optional<Organization> findByIdOptional(OrganizationId id) {
    return this.findByIdOptional(id.id())
        .map(jpaMapper::toDomain);
  }

  @Override
  public Set<Organization> findAllOrganizations() {
    return this.findAll()
        .stream()
        .map(jpaMapper::toDomain)
        .collect(Collectors.toSet());
  }

  @Override
  @Transactional
  public void save(Organization organization) {
    var orgToSave = jpaMapper.toPersistence(organization);
    this.persist(orgToSave);
  }


}
