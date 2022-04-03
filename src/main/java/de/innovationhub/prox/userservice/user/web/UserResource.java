package de.innovationhub.prox.userservice.user.web;

import de.innovationhub.prox.userservice.core.Status;
import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.user.dto.UserProfileBriefCollectionResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileRequestDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.service.UserService;
import io.quarkus.security.Authenticated;
import java.io.IOException;
import java.util.UUID;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

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
  public Iterable<UserSearchResponseDto> findUser(@QueryParam("q") String searchQuery) {
    return this.userService.search(searchQuery);
  }

  @GET
  @Authenticated
  @Path("{id}")
  public UserSearchResponseDto getKeycloakService(@PathParam("id") UUID id) {
    return this.userService.findById(id).orElseThrow(() -> new WebApplicationException(404));
  }

  @GET
  @Path("profiles")
  public UserProfileBriefCollectionResponseDto getAllProfiles() {
    return this.userService.findAll();
  }

  @GET
  @Path("{id}/profile")
  public UserProfileResponseDto getUserProfile(@PathParam("id") UUID id) {
    return this.userService
        .findProfileByUserId(id)
        .orElseThrow(() -> new WebApplicationException(404));
  }

  @POST
  @Status(201)
  @Authenticated
  @Path("{id}/profile")
  public UserProfileResponseDto createUserProfile(
      @PathParam("id") UUID id, UserProfileRequestDto request) {
    return this.userService.saveUserProfile(id, request);
  }

  @GET
  @Path("{id}/profile/avatar")
  public Response getAvatar(@PathParam(value = "id") UUID id) {
    try {
      return userService.getAvatar(id);
    } catch (IOException e) {
      throw new WebApplicationException(500);
    }
  }

  @POST
  @Authenticated
  @Path("{id}/profile/avatar")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response setAvatar(
      @PathParam(value = "id") UUID id, @MultipartForm @Valid FormDataBody formDataBody) {
    try {
      userService.setAvatar(id, formDataBody);
      return Response.ok().status(Response.Status.CREATED).build();
    } catch (IOException e) {
      throw new WebApplicationException(500);
    }
  }
}
