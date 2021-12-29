package de.innovationhub.prox.userservice.domain.user.service;

import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.exception.AmbiguousPrincipalException;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationRole;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserService {
  private final UserRepository userRepository;
  private final OrganizationRepository organizationRepository;

  @Inject
  public UserService(
      UserRepository userRepository,
      OrganizationRepository organizationRepository) {
    this.userRepository = userRepository;
    this.organizationRepository = organizationRepository;
  }

  public User create(String principal) {
    if(userRepository.existByPrincipal(principal)) {
      throw new AmbiguousPrincipalException("Principal %s already exists".formatted(principal));
    }
    var user = new User(principal);
    this.userRepository.create(user);
    return user;
  }

  public User addMembership(String principal, UUID organizationId, OrganizationRole role) {
    var user = this.userRepository.findByPrincipalOptional(principal).orElseThrow();
    var org = this.organizationRepository.findByIdOptional(organizationId).orElseThrow();

    var membership = new OrganizationMembership(role);
    user.addMembership(org, membership);
    this.userRepository.update(principal, user);
    return user;
  }
}
