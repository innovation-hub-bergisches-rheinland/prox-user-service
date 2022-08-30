package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import de.innovationhub.prox.userservice.user.service.KeycloakUserIdentityService;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.Record;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {
  private final Logger LOGGER = Logger.getLogger(UserRepositoryImpl.class);

  private final KeycloakUserIdentityService userIdentityService;
  private final UserMapper userMapper;
  private final UserProfileRepository userProfileRepository;

  @Inject
  @Channel("users")
  Emitter<Record<String, User>> userEmitter;

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
                profile ->
                    matchingIdentities.stream().noneMatch(u -> u.getId().equals(profile.getId())))
            .toList();

    var foundUsers = new ArrayList<>(matchingIdentities);
    foundUsers.addAll(matchingProfiles);
    foundUsers.sort(Comparator.comparing(User::getName));

    return foundUsers;
  }

  @Override
  public List<User> searchByEmail(String email) {
    return StreamSupport.stream(userIdentityService.searchByMail(email).spliterator(), false)
        .map(this::toUser)
        .toList();
  }

  @Override
  @Transactional
  public void save(@Valid User user) {
    // We only want to save the user profile as everything else is managed by Keycloak.
    if (user.getProfile() != null) {
      userProfileRepository.save(user.getProfile());
    }
    var userEvent = userEmitter.send(Record.of(user.getId().toString(), user));
    Uni.createFrom()
        .completionStage(userEvent)
        .onFailure()
        .invoke(
            e -> {
              LOGGER.errorf(
                  e, "Error while sending user event for user '%s' to kafka", user.getId());
              QuarkusTransaction.setRollbackOnly();
            })
        .onItem()
        .invoke(
            () -> {
              LOGGER.debugf("User event for user '%s' sent to kafka", user.getId());
            })
        .await()
        .atMost(Duration.of(10, ChronoUnit.SECONDS));
  }

  @Override
  public List<UserProfile> findAllProfiles() {
    return userProfileRepository.findAll();
  }

  private User toUser(UserRepresentation userRepresentation, Optional<UserProfile> profile) {
    return userMapper.toEntity(userRepresentation, profile.orElse(null));
  }

  private User toUser(UserRepresentation userRepresentation) {
    return toUser(
        userRepresentation,
        userProfileRepository.findProfileByUserId(UUID.fromString(userRepresentation.getId())));
  }
}
