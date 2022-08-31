package de.innovationhub.prox.userservice.user.web;

import de.innovationhub.prox.userservice.core.Status;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.service.UserService;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

@Path("users")
@RolesAllowed("admin")
public class UserAdminResource {
  private final UserService userService;

  @Inject
  public UserAdminResource(UserService userService) {
    this.userService = userService;
  }

  @POST
  @Status(200)
  @Path("{id}/reconciliation")
  public UserSearchResponseDto reconciliation(@PathParam("id") UUID id) {
    this.userService.reconcile(id);
    return this.userService.findById(id).orElseThrow(() -> new WebApplicationException(404));
  }
}
