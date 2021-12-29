package de.innovationhub.prox.userservice.domain.user.service;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.exception.AmbiguousPrincipalException;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserService {
  private final UserRepository userRepository;

  @Inject
  public UserService(
      UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User create(String principal) {
    if(userRepository.existByPrincipal(principal)) {
      throw new AmbiguousPrincipalException("Principal %s already exists".formatted(principal));
    }
    var user = new User(UUID.randomUUID(), principal);
    this.userRepository.save(user);
    return user;
  }
}
