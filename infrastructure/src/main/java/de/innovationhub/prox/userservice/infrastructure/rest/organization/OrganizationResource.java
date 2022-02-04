package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import de.innovationhub.prox.userservice.application.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.infrastructure.core.Status;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.OrganizationRestMessageMapper;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PostOrganizationDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationCollectionDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationDto;
import io.quarkus.security.Authenticated;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
  public OrganizationDto create(PostOrganizationDto jsonRequest) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    var response = this.organizationService.createOrganization(jsonRequest.name(), userId);
    return this.messageMapper.toResponse(response);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationDto findById(@PathParam(value = "id") UUID id) {
    if(id == null) {
      throw new WebApplicationException("Provided ID is null", 400);
    }
    return this.organizationService.findById(id)
        .map(messageMapper::toResponse)
        .orElseThrow(() -> new WebApplicationException(404));
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationCollectionDto findAll() {
    var mappedCollection = messageMapper.toResponse(organizationService.findAll());
    return new OrganizationCollectionDto(mappedCollection);
  }
}
