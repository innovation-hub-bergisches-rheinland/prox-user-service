package de.innovationhub.prox.userservice.domain.user.repository;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  Optional<User> findByIdOptional(UUID uuid);
  void save(User user);
}