package de.innovationhub.prox.userservice.organization.web;

import de.innovationhub.prox.userservice.core.Status;
import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewAllOrganizationMembershipsDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import io.quarkus.security.Authenticated;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import javax.validation.Valid;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

@Path("organizations")
public class OrganizationResource {
  private final OrganizationService organizationService;

  @Inject
  public OrganizationResource(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @POST
  @Authenticated
  @Status(201)
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ViewOrganizationDto create(CreateOrganizationDto jsonRequest) {
    return this.organizationService.createOrganization(jsonRequest);
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ViewOrganizationDto findById(@PathParam(value = "id") UUID id) {
    if (id == null) {
      throw new WebApplicationException("Provided ID is null", 400);
    }
    return this.organizationService
        .findById(id)
        .orElseThrow(() -> new WebApplicationException(404));
  }

  @GET
  @Path("{id}/avatar")
  public Response getOrganizationAvatar(@PathParam(value = "id") UUID id) {
    try {
      var fileObj = organizationService.getOrganizationAvatar(id);
      ResponseBuilder response = Response.ok(fileObj.getData());
      response.header("Content-Disposition", "attachment;filename=" + fileObj.getKey());
      response.header("Content-Type", fileObj.getMimeType());

      if (!fileObj.getMimeType().equalsIgnoreCase("image/png")
          && !fileObj.getMimeType().equalsIgnoreCase("image/jpeg"))
        throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);

      return response.build();
    } catch (IOException e) {
      throw new WebApplicationException(500);
    }
  }

  @POST
  @Authenticated
  @Path("{id}/avatar")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response setOrganizationAvatar(
      @PathParam(value = "id") UUID id, @MultipartForm @Valid FormDataBody formDataBody) {
    try {
      organizationService.setOrganizationAvatar(id, formDataBody);
      return Response.ok().status(Response.Status.CREATED).build();
    } catch (IOException e) {
      throw new WebApplicationException(500);
    }
  }

  @PUT
  @Authenticated
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public ViewOrganizationDto updateOrganization(
      @PathParam(value = "id") UUID id, CreateOrganizationDto jsonRequest) {
    if (id == null) {
      throw new WebApplicationException("Provided ID is null", 400);
    }
    return this.organizationService.updateOrganization(id, jsonRequest);
  }

  @GET
  @Authenticated
  @Path("{id}/memberships")
  @Produces(MediaType.APPLICATION_JSON)
  public ViewAllOrganizationMembershipsDto getOrganizationMemberships(
      @PathParam("id") UUID organizationId) {
    try {
      return this.organizationService.getOrganizationMemberships(organizationId);
    } catch (ForbiddenOrganizationAccessException e) {
      throw new WebApplicationException(e, 403);
    } catch (OrganizationNotFoundException e) {
      throw new WebApplicationException(e, 404);
    }
  }

  @POST
  @Authenticated
  @Path("{id}/memberships")
  @Status(201)
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public ViewOrganizationMembershipDto addOrganizationMember(
      @PathParam("id") UUID organizationId,
      @RequestBody CreateOrganizationMembershipDto memberDto) {
    try {
      return this.organizationService.createOrganizationMembership(organizationId, memberDto);
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
  public ViewOrganizationMembershipDto updateOrganizationMember(
      @PathParam("id") UUID organizationId,
      @PathParam("memberId") UUID memberId,
      @RequestBody UpdateOrganizationMembershipDto updateDto) {

    try {
      return this.organizationService.updateOrganizationMembership(
          organizationId, memberId, updateDto);
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
  public void removeOrganizationMember(
      @PathParam("id") UUID organizationId, @PathParam("memberId") UUID memberId) {
    try {
      this.organizationService.deleteOrganizationMembership(organizationId, memberId);
    } catch (ForbiddenOrganizationAccessException e) {
      throw new WebApplicationException(e, 403);
    } catch (OrganizationNotFoundException e) {
      throw new WebApplicationException(e, 404);
    }
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<ViewOrganizationDto> findAll() {
    return this.organizationService.findAll();
  }
}
