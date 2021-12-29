package de.innovationhub.prox.userservice.domain.membership.repository;

import de.innovationhub.prox.userservice.domain.membership.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.Optional;

public interface OrganizationMembershipRepository {
  Optional<OrganizationMembership> getOrganizationMembershipOptional(Organization organization, User user);
  boolean existOrganizationMembershipOptional(Organization organization, User user);
  void save(OrganizationMembership membership);
}
