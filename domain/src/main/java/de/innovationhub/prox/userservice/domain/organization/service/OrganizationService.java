package de.innovationhub.prox.userservice.domain.organization.service;

import de.innovationhub.prox.userservice.domain.membership.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.membership.repository.OrganizationMembershipRepository;
import de.innovationhub.prox.userservice.domain.membership.service.OrganizationMembershipService;
import de.innovationhub.prox.userservice.domain.membership.vo.OrganizationRole;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.service.UserService;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final UserService userService;
  private final OrganizationMembershipService organizationMembershipService;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      UserService userService,
      OrganizationMembershipService organizationMembershipService) {
    this.organizationRepository = organizationRepository;
    this.userService = userService;
    this.organizationMembershipService = organizationMembershipService;
  }

  public Organization create(String name, String ownerPrincipal) {
    var owner= this.userService.getOptional(ownerPrincipal)
        .orElse(new User(ownerPrincipal));
    var org = new Organization(UUID.randomUUID(), name);
    this.organizationMembershipService.create(org, owner, OrganizationRole.OWNER);
    return org;
  }
}
