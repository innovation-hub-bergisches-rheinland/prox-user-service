package de.innovationhub.prox.userservice.user.web;

import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import de.innovationhub.prox.userservice.user.service.KeycloakService;
import io.quarkus.security.Authenticated;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;

@Path("users")
public class UserResource {
  private final KeycloakService keycloakService;

  @Inject
  public UserResource(KeycloakService keycloakService) {
    this.keycloakService = keycloakService;
  }

  @GET
  @Authenticated
  @Path("search")
  public Iterable<UserResponseDto> findUser(@QueryParam("q") String searchQuery) {
    return this.keycloakService.search(searchQuery);
  }

  @GET
  @Authenticated
  @Path("{id}")
  public UserResponseDto getKeycloakService(@PathParam("id") UUID id) {
    return keycloakService.findById(id).orElseThrow(() -> new WebApplicationException(404));
  }
}
