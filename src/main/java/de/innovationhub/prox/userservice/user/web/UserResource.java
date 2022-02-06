package de.innovationhub.prox.userservice.user.web;

import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import de.innovationhub.prox.userservice.user.service.UserService;
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
  private final UserService userService;

  @Inject
  public UserResource(UserService userService) {
    this.userService = userService;
  }

  @GET
  @Authenticated
  @Path("search")
  public Iterable<UserResponseDto> findUser(@QueryParam("q") String searchQuery) {
    return this.userService.search(searchQuery);
  }

  @GET
  @Authenticated
  @Path("{id}")
  public UserResponseDto getKeycloakService(@PathParam("id") UUID id) {
    return userService.findById(id).orElseThrow(() -> new WebApplicationException(404));
  }
}
