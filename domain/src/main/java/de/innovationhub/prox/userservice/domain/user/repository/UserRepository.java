package de.innovationhub.prox.userservice.domain.user.repository;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  Optional<User> findByPrincipalOptional(String principal);
  boolean existByPrincipal(String principal);
  void save(User user);
}
