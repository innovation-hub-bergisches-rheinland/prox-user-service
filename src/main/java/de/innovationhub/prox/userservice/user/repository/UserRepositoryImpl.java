package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import de.innovationhub.prox.userservice.user.service.KeycloakUserIdentityService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

  private final KeycloakUserIdentityService userIdentityService;
  private final UserMapper userMapper;
  private final UserProfileRepository userProfileRepository;

  public UserRepositoryImpl(
      KeycloakUserIdentityService userIdentityService,
      UserMapper userMapper,
      UserProfileRepository userProfileRepository) {
    this.userIdentityService = userIdentityService;
    this.userMapper = userMapper;
    this.userProfileRepository = userProfileRepository;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return userIdentityService
        .findById(id)
        .map(u -> this.toUser(u, userProfileRepository.findProfileByUserId(id)));
  }

  @Override
  public boolean existsById(UUID id) {
    return userIdentityService.existsById(id);
  }

  @Override
  public List<User> search(String query) {
    var matchingIdentities =
        StreamSupport.stream(userIdentityService.search(query).spliterator(), false)
            .map(this::toUser)
            .toList();
    var matchingProfiles =
        userProfileRepository.search(query).stream()
            .map(profile -> userIdentityService.findById(profile.getUserId()))
            .filter(Optional::isPresent)
            .map(opt -> this.toUser(opt.get()))
            .filter(
                profile -> matchingIdentities.stream().noneMatch(u -> u.id().equals(profile.id())))
            .toList();

    var foundUsers = new ArrayList<>(matchingIdentities);
    foundUsers.addAll(matchingProfiles);
    foundUsers.sort(Comparator.comparing(User::name));

    return foundUsers;
  }

  @Override
  public List<User> searchByEmail(String email) {
    return StreamSupport.stream(userIdentityService.searchByMail(email).spliterator(), false)
        .map(this::toUser)
        .toList();
  }

  private User toUser(UserRepresentation userRepresentation, Optional<UserProfile> profile) {
    return userMapper.toEntity(userRepresentation, profile);
  }

  private User toUser(UserRepresentation userRepresentation) {
    return toUser(
        userRepresentation,
        userProfileRepository.findProfileByUserId(UUID.fromString(userRepresentation.getId())));
  }
}
