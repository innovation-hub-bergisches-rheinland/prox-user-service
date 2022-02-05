package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import de.innovationhub.prox.userservice.application.organization.dto.request.DeleteOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.application.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.application.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.infrastructure.core.Status;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.OrganizationRestMessageMapper;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PostOrganizationDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PostOrganizationMemberDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PutOrganizationMemberDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationCollectionDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationMemberDto;
import io.quarkus.security.Authenticated;
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

  @POST
  @Authenticated
  @Path("{id}/memberships")
  @Status(201)
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public OrganizationMemberDto addOrganizationMember(
      @PathParam("id") UUID organizationId,
      @RequestBody PostOrganizationMemberDto memberDto
  ) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    var request = this.messageMapper.toRequest(memberDto);

    try {
      var response = this.organizationService.createOrganizationMembership(organizationId, request, userId);
      return this.messageMapper.toResponse(response);
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
  public OrganizationMemberDto updateOrganizationMember(
      @PathParam("id") UUID organizationId,
      @PathParam("memberId") UUID memberId,
      @RequestBody PutOrganizationMemberDto updateDto
  ) {
    var userId = UUID.fromString(jsonWebToken.getSubject());
    var request = this.messageMapper.toRequest(updateDto, memberId);

    try {
      var response = this.organizationService.updateOrganizationMembership(organizationId, request, userId);
      return this.messageMapper.toResponse(response);
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
      this.organizationService.deleteOrganizationMembership(organizationId, new DeleteOrganizationMembershipRequest(memberId), userId);
    } catch (ForbiddenOrganizationAccessException e) {
      throw new WebApplicationException(e, 403);
    } catch (OrganizationNotFoundException e) {
      throw new WebApplicationException(e, 404);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public OrganizationCollectionDto findAll() {
    var mappedCollection = messageMapper.toResponse(organizationService.findAll());
    return new OrganizationCollectionDto(mappedCollection);
  }
}
