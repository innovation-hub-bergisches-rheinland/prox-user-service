package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.service.KeycloakUserIdentityService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KeycloakUserRepository implements UserRepository {

  private final KeycloakUserIdentityService userIdentityService;
  private final UserMapper userMapper;
  private final UserProfileRepository userProfileRepository;

  public KeycloakUserRepository(
      KeycloakUserIdentityService userIdentityService, UserMapper userMapper,
      UserProfileRepository userProfileRepository) {
    this.userIdentityService = userIdentityService;
    this.userMapper = userMapper;
    this.userProfileRepository = userProfileRepository;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return userIdentityService.findById(id)
        .map(u -> userMapper.toEntity(u, userProfileRepository.findProfileByUserId(id)));
  }

  @Override
  public boolean existsById(UUID id) {
    return userIdentityService.existsById(id);
  }

  @Override
  public List<User> search(String query) {
    return StreamSupport.stream(userIdentityService.search(query).spliterator(), false)
        .map(u -> userMapper.toEntity(u, userProfileRepository.findProfileByUserId(UUID.fromString(u.getId()))))
        .toList();
  }

  @Override
  public List<User> searchByEmail(String email) {
    return StreamSupport.stream(userIdentityService.searchByMail(email).spliterator(), false)
        .map(u -> userMapper.toEntity(u, userProfileRepository.findProfileByUserId(UUID.fromString(u.getId()))))
        .toList();
  }
}
