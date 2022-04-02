package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository {
  Optional<UserProfile> findProfileByUserId(UUID userId);

  void save(UserProfile userProfile);
}
