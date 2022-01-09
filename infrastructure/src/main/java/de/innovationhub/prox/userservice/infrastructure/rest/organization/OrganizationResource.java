package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.request.FindOrganizationByIdRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.OrganizationResponse;
import de.innovationhub.prox.userservice.application.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.infrastructure.core.Status;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.message.OrganizationRestMessageMapper;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.message.request.PostOrganizationJsonRequest;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.message.response.OrganizationJsonResponse;
import io.quarkus.security.Authenticated;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("organizations")
public class OrganizationResource {
  private final OrganizationService organizationService;
  private final OrganizationRestMessageMapper messageMapper;

  @Inject
  JsonWebToken jsonWebToken;

  @Inject
  public OrganizationResource(
      OrganizationService organizationService,
      OrganizationRestMessageMapper messageMapper) {
    this.organizationService = organizationService;
    this.messageMapper = messageMapper;
  }

  @POST
  @Authenticated
  @Status(201)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationJsonResponse create(PostOrganizationJsonRequest jsonRequest) {
    var request = messageMapper.toRequest(jsonRequest, jsonWebToken.getSubject());
    var response = this.organizationService.createOrganization(request);
    return this.messageMapper.toResponse(response);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationJsonResponse findById(@QueryParam("id") UUID id) {
    var request = new FindOrganizationByIdRequest(id);
    return this.organizationService.findById(request)
        .map(messageMapper::toResponse)
        .orElseThrow(() -> new WebApplicationException(404));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Set<OrganizationJsonResponse> findAll() {
    return this.organizationService
        .findAll().stream()
        .map(messageMapper::toResponse)
        .collect(Collectors.toSet());
  }
}
