package de.innovationhub.prox.userservice.domain.membership.service;

import de.innovationhub.prox.userservice.domain.membership.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.membership.repository.OrganizationMembershipRepository;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.membership.vo.OrganizationRole;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationMembershipService {

  private final OrganizationMembershipRepository membershipRepository;

  @Inject
  public OrganizationMembershipService(
      OrganizationMembershipRepository membershipRepository) {
    this.membershipRepository = membershipRepository;
  }

  public OrganizationMembership create(Organization organization, User user,
      OrganizationRole organizationRole) {
    if (membershipRepository.existOrganizationMembershipOptional(organization, user)) {
      throw new RuntimeException();
    }
    var membership = new OrganizationMembership(UUID.randomUUID(), organization, user,
        organizationRole);
    this.membershipRepository.save(membership);
    return membership;
  }
}
