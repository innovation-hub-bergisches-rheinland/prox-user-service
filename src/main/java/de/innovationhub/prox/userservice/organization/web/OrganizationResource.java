package de.innovationhub.prox.userservice.organization.web;

import de.innovationhub.prox.userservice.core.Status;
import de.innovationhub.prox.userservice.organization.dto.OrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import io.quarkus.security.Authenticated;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("organizations")
public class OrganizationResource {
  private final OrganizationService organizationService;

  @Inject
  JsonWebToken jsonWebToken;

  @Inject
  public OrganizationResource(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @POST
  @Authenticated
  @Status(201)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationDto create(OrganizationDto jsonRequest) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    return this.organizationService.createOrganization(jsonRequest.name(), userId);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationDto findById(@PathParam(value = "id") UUID id) {
    if(id == null) {
      throw new WebApplicationException("Provided ID is null", 400);
    }
    return this.organizationService.findById(id)
        .orElseThrow(() -> new WebApplicationException(404));
  }

  @POST
  @Authenticated
  @Path("{id}/memberships")
  @Status(201)
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public OrganizationMembershipDto addOrganizationMember(
      @PathParam("id") UUID organizationId,
      @RequestBody OrganizationMembershipDto memberDto
  ) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    try {
      return this.organizationService.createOrganizationMembership(organizationId, memberDto, userId);
    } catch (ForbiddenOrganizationAccessException e) {
      throw new WebApplicationException(e, 403);
    } catch (OrganizationNotFoundException e) {
      throw new WebApplicationException(e, 404);
    }
  }

  @PUT
  @Authenticated
  @Path("{id}/memberships/{memberId}")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public OrganizationMembershipDto updateOrganizationMember(
      @PathParam("id") UUID organizationId,
      @PathParam("memberId") UUID memberId,
      @RequestBody OrganizationMembershipDto updateDto
  ) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    updateDto = new OrganizationMembershipDto(memberId, updateDto.role());

    try {
      return this.organizationService.updateOrganizationMembership(organizationId, updateDto, userId);
    } catch (ForbiddenOrganizationAccessException e) {
      throw new WebApplicationException(e, 403);
    } catch (OrganizationNotFoundException e) {
      throw new WebApplicationException(e, 404);
    }
  }

  @DELETE
  @Authenticated
  @Path("{id}/memberships/{memberId}")
  @Status(204)
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public void removeOrganizationMember(
      @PathParam("id") UUID organizationId,
      @PathParam("memberId") UUID memberId
  ) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    try {
      this.organizationService.deleteOrganizationMembership(organizationId, memberId, userId);
    } catch (ForbiddenOrganizationAccessException e) {
      throw new WebApplicationException(e, 403);
    } catch (OrganizationNotFoundException e) {
      throw new WebApplicationException(e, 404);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<OrganizationDto> findAll() {
    return this.organizationService.findAll();
  }
}
