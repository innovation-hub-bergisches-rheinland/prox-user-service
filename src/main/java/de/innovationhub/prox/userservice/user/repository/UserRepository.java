package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

public interface UserRepository {
  Optional<User> findById(UUID id);

  boolean existsById(UUID id);

  List<User> search(String query);

  List<User> searchByEmail(String email);

  void save(@Valid User user);

  List<UserProfile> findAllProfiles();
}
