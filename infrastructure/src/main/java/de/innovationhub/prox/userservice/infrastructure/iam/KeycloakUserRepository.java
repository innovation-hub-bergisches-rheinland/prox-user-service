package de.innovationhub.prox.userservice.infrastructure.iam;

import de.innovationhub.prox.userservice.domain.user.entity.ProxUser;
import de.innovationhub.prox.userservice.domain.user.repository.ProxUserRepository;
import de.innovationhub.prox.userservice.infrastructure.iam.mapper.UserMapper;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class KeycloakUserRepository implements ProxUserRepository {

  private final KeycloakService keycloakService;
  private final UserMapper userMapper;

  @Inject
  public KeycloakUserRepository(
      KeycloakService keycloakService,
      UserMapper userMapper) {
    this.keycloakService = keycloakService;
    this.userMapper = userMapper;
  }

  @Override
  public Optional<ProxUser> findById(UUID id) {
    return this.keycloakService.findById(id)
        .map(this.userMapper::toDomain);
  }

  @Override
  public Set<ProxUser> search(String query) {
    return this.userMapper.toSet(
        StreamSupport.stream(this.keycloakService.search(query).spliterator(), false));
  }
}
