package de.innovationhub.prox.userservice.organization.repository;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
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
  public List<Organization> findAllWithUserAsMember(UUID id) {
    // TODO: For whatever reason this doesn't work - it seems to be that the "or" operator is not
    // used correctly
    // return organizationPanacheRepository.find("from Organization o where (o.owner = ?1 or
    // key(o.members) = ?1)", id).stream().distinct().toList();
    return this.findAll().stream()
        .filter(it -> it.getMembers().containsKey(id))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void save(Organization organization) {
    this.organizationPanacheRepository.persist(organization);
  }
}
