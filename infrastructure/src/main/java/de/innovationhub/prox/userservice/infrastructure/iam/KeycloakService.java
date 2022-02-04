package de.innovationhub.prox.userservice.infrastructure.iam;

import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class KeycloakService {
  private final RealmResource realmResource;
  private final UsersResource usersResource;

  @Inject
  public KeycloakService(RealmResource realmResource) {
    this.realmResource = realmResource;
    this.usersResource = realmResource.users();
  }

  public Optional<UserRepresentation> findById(UUID id) {
    try {
      var result = this.usersResource.get(id.toString()).toRepresentation();
      return Optional.of(result);
    } catch (WebApplicationException e) {
      // If we receive a 404 status, the user doesn't exist and we can return an empty result
      if(e.getResponse().getStatus() == 404) {
        return Optional.empty();
      }

      // Otherwise the response is errornous and therefore we need to rethrow the exception
      throw e;
    }
  }

  public Iterable<UserRepresentation> search(String query) {
    return this.realmResource.users().search(query, 0, 100, true);
  }
}
