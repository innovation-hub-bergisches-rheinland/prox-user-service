package de.innovationhub.prox.userservice.organization.web;

import de.innovationhub.prox.userservice.core.Status;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;

@Path("organizations")
@RolesAllowed("admin")
public class OrganizationAdminResource {
  private final OrganizationService organizationService;

  @Inject
  public OrganizationAdminResource(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @POST
  @Status(200)
  @Path("{id}/reconciliation")
  public ViewOrganizationDto reconciliation(@PathParam("id") UUID id) {
    this.organizationService.reconcile(id);
    return this.organizationService
        .findById(id)
        .orElseThrow(() -> new WebApplicationException(404));
  }
}
