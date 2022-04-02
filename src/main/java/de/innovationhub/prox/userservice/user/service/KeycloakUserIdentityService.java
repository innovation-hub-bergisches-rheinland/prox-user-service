package de.innovationhub.prox.userservice.user.service;

import io.quarkus.cache.CacheResult;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class KeycloakUserIdentityService implements UserIdentityService<UserRepresentation> {
  private final RealmResource realmResource;
  private final UsersResource usersResource;

  @Inject
  public KeycloakUserIdentityService(RealmResource realmResource) {
    this.realmResource = realmResource;
    this.usersResource = realmResource.users();
  }

  @CacheResult(cacheName = "keycloak-user-cache")
  public Optional<UserRepresentation> findById(UUID id) {
    try {
      return Optional.of(this.usersResource.get(id.toString()).toRepresentation());
    } catch (NotFoundException e) {
      return Optional.empty();
    }
  }

  public boolean existsById(UUID id) {
    return this.findById(id).isPresent();
  }

  @CacheResult(cacheName = "keycloak-users-search-cache")
  public Iterable<UserRepresentation> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true);
  }

  @CacheResult(cacheName = "keycloak-users-search-cache-mail")
  public Iterable<UserRepresentation> searchByMail(String query) {
    return StreamSupport.stream(this.search(query).spliterator(), false)
        .filter(u -> u.getEmail().equalsIgnoreCase(query))
        .toList();
  }
}
