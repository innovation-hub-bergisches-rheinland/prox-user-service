package de.innovationhub.prox.userservice.infrastructure.rest.organization.message;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.OrganizationResponse;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.message.request.PostOrganizationJsonRequest;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.message.response.OrganizationJsonResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface OrganizationRestMessageMapper {
  OrganizationRestMessageMapper INSTANCE = Mappers.getMapper(OrganizationRestMessageMapper.class);

  @Mapping(target = "name", source = "json.name")
  @Mapping(target = "ownerPrincipal", source = "principal")
  CreateOrganizationRequest toRequest(PostOrganizationJsonRequest json, String principal);

  @Mapping(target = "owner", source = "response.ownerPrincipal")
  OrganizationJsonResponse toResponse(OrganizationResponse response);
}
