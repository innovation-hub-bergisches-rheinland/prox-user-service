package de.innovationhub.prox.userservice.infrastructure.iam;

import de.innovationhub.prox.userservice.infrastructure.iam.dto.UserResponseDto;
import de.innovationhub.prox.userservice.infrastructure.iam.mapper.UserMapper;
import io.quarkus.cache.CacheResult;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class KeycloakService {
  private final RealmResource realmResource;
  private final UsersResource usersResource;
  private final UserMapper userMapper;

  @Inject
  public KeycloakService(RealmResource realmResource,
      UserMapper userMapper) {
    this.realmResource = realmResource;
    this.usersResource = realmResource.users();
    this.userMapper = userMapper;
  }

  @CacheResult(cacheName = "keycloak-user-cache")
  public Optional<UserResponseDto> findById(UUID id) {
    try {
      var result = this.usersResource.get(id.toString()).toRepresentation();
      return Optional.of(this.userMapper.toDto(result));
    } catch (NotFoundException e) {
      return Optional.empty();
    }
  }

  @CacheResult(cacheName = "keycloak-users-search-cache")
  public Iterable<UserResponseDto> search(String query) {
    var searchResult = this.realmResource.users().search(query, 0, 100, true);
    return this.userMapper.toDtoSet(searchResult.stream());
  }
}
