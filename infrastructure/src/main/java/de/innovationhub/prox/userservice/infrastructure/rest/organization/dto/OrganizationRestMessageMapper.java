package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto;

import de.innovationhub.prox.userservice.application.organization.dto.request.CreateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.application.organization.dto.response.CreateOrganizationMembershipResponse;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PostOrganizationDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PostOrganizationMemberDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationMemberDto;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface OrganizationRestMessageMapper {
  OrganizationRestMessageMapper INSTANCE = Mappers.getMapper(OrganizationRestMessageMapper.class);

  @Mapping(target = "id", source = "organization.id.id")
  @Mapping(target = "owner", source = "organization.owner.id")
  OrganizationDto toResponse(Organization organization);

  Set<OrganizationDto> toResponse(Set<Organization> organization);

  CreateOrganizationMembershipRequest toRequest(PostOrganizationMemberDto dto);

  OrganizationMemberDto toResponse(CreateOrganizationMembershipResponse response);
}
