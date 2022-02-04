package de.innovationhub.prox.userservice.domain.user.repository;

import de.innovationhub.prox.userservice.domain.user.entity.ProxUser;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Repository for the user entity
 */
public interface ProxUserRepository {
  /**
   * Lookup a user with the given id
   * @param id ID
   * @return User - empty if no user was found
   */
  Optional<ProxUser> findById(UUID id);

  /**
   * Searches for a user with a given query string
   * @param query search query - look at the concrete implementations for more details
   * @return a collection of unique user results
   */
  Set<ProxUser> search(String query);
}
