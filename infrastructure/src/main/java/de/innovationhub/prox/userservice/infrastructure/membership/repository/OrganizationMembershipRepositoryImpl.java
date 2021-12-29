package de.innovationhub.prox.userservice.infrastructure.membership.repository;

import de.innovationhub.prox.userservice.domain.membership.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.membership.repository.OrganizationMembershipRepository;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.infrastructure.membership.mapper.OrganizationMembershipJpaMapper;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationMembershipRepositoryImpl implements OrganizationMembershipRepository {
  private final OrganizationMembershipPanacheRepository membershipPanacheRepository;
  private final OrganizationMembershipJpaMapper jpaMapper;

  @Inject
  public OrganizationMembershipRepositoryImpl(
      OrganizationMembershipPanacheRepository membershipPanacheRepository,
      OrganizationMembershipJpaMapper jpaMapper) {
    this.membershipPanacheRepository = membershipPanacheRepository;
    this.jpaMapper = jpaMapper;
  }

  @Override
  public Optional<OrganizationMembership> getOrganizationMembershipOptional(
      Organization organization, User user) {
    return membershipPanacheRepository
        .find("organization.id = ?1 and user.principal = ?2", organization.getId(), user.getPrincipal())
        .firstResultOptional()
        .map(jpaMapper::toDomain);
  }

  @Override
  public boolean existOrganizationMembershipOptional(Organization organization, User user) {
    return membershipPanacheRepository.find("organization.id = ?1 and user.principal = ?2", organization.getId(), user.getPrincipal())
        .count() > 0;
  }

  @Override
  public void save(OrganizationMembership membership) {
    var jpaModel = jpaMapper.toPersistence(membership);
    this.membershipPanacheRepository.persist(jpaModel);
  }
}
