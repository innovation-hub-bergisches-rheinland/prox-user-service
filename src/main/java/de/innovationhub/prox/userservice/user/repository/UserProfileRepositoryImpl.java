package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserProfileRepositoryImpl implements UserProfileRepository {
  private final UserProfilePanacheRepository panacheRepository;

  @Inject
  public UserProfileRepositoryImpl(UserProfilePanacheRepository panacheRepository) {
    this.panacheRepository = panacheRepository;
  }

  @Override
  public Optional<UserProfile> findProfileByUserId(UUID userId) {
    return panacheRepository.findByIdOptional(userId);
  }

  @Override
  public void save(UserProfile userProfile) {
    panacheRepository.persist(userProfile);
  }
}
