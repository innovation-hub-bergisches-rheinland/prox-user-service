package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
  Optional<User> findById(UUID id);

  boolean existsById(UUID id);

  List<User> search(String query);

  List<User> searchByEmail(String email);
}
