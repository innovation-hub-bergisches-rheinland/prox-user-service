package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import io.quarkus.panache.common.Sort;
import io.quarkus.panache.common.Sort.Direction;
import java.util.List;
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

  @Override
  public List<UserProfile> findAll() {
    return panacheRepository.findAll(Sort.by("name", Direction.Ascending)).list();
  }

  @Override
  public List<UserProfile> search(String query) {
    return panacheRepository.find("lower(name) like concat ('%', lower(?1), '%')", query).list();
  }
}
