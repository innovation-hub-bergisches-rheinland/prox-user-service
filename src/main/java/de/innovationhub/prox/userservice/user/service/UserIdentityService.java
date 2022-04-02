package de.innovationhub.prox.userservice.user.service;

import java.util.Optional;
import java.util.UUID;

public interface UserIdentityService<T> {
  Optional<T> findById(UUID id);

  boolean existsById(UUID id);

  Iterable<T> search(String query);
}
