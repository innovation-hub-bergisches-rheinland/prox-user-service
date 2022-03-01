package de.innovationhub.prox.userservice.user.web;

import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.user.service.UserService;
import io.quarkus.security.Authenticated;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("user")
public class AuthenticatedUserResource {
  private final UserService userService;

  @Inject
  public AuthenticatedUserResource(UserService userService) {
    this.userService = userService;
  }

  @GET
  @Authenticated
  @Produces(MediaType.APPLICATION_JSON)
  @Path("organizations")
  public List<ViewOrganizationDto> listOrganizationsForAuthenticatedUser() {
    return this.userService.findOrganizationsOfAuthenticatedUser();
  }
}
