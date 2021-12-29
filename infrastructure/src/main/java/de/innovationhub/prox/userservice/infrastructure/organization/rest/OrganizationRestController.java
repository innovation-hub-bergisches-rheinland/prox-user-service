package de.innovationhub.prox.userservice.infrastructure.organization.rest;

import de.innovationhub.prox.userservice.application.organization.controller.OrganizationController;
import de.innovationhub.prox.userservice.application.organization.manager.OrganizationManager;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.CreateOrganizationResponse;
import de.innovationhub.prox.userservice.infrastructure.core.web.Status;
import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("/organizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrganizationRestController implements OrganizationController {

  private final OrganizationManager organizationManager;

  @Inject
  JsonWebToken jwt;

  @Inject
  public OrganizationRestController(
      OrganizationManager organizationManager) {
    this.organizationManager = organizationManager;
  }

  @POST
  @Authenticated
  @Status(201)
  @Override
  public CreateOrganizationResponse createOrganization(
      CreateOrganizationRequest request) {
    return organizationManager.createOrganization(request, jwt.getSubject());
  }
}
