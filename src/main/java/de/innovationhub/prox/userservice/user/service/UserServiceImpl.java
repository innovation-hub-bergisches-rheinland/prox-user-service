package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {
  private final UserIdentityService userIdentityService;
  private final OrganizationService organizationService;
  private final SecurityIdentity securityIdentity;

  @Inject
  public UserServiceImpl(
      UserIdentityService userIdentityService,
      OrganizationService organizationService,
      SecurityIdentity securityIdentity) {
    this.userIdentityService = userIdentityService;
    this.organizationService = organizationService;
    this.securityIdentity = securityIdentity;
  }

  @Override
  public Optional<UserSearchResponseDto> findById(UUID id) {
    return this.userIdentityService.findById(id);
  }

  @Override
  public boolean existsById(UUID id) {
    return this.userIdentityService.existsById(id);
  }

  @Override
  public Iterable<UserSearchResponseDto> search(String query) {
    return this.userIdentityService.search(query);
  }

  @Override
  public List<ViewOrganizationDto> findOrganizationsOfAuthenticatedUser() {
    var userId = UUID.fromString(this.securityIdentity.getPrincipal().getName());
    return this.organizationService.findOrganizationsWhereUserIsMember(userId);
  }
}
