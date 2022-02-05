package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto;

import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.response.OrganizationMembershipResponse;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PostOrganizationMemberDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request.PutOrganizationMemberDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationDto;
import de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response.OrganizationMemberDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface OrganizationRestMessageMapper {
  OrganizationRestMessageMapper INSTANCE = Mappers.getMapper(OrganizationRestMessageMapper.class);

  @Mapping(target = "id", source = "organization.id")
  @Mapping(target = "owner", source = "organization.owner")
  OrganizationDto toResponse(Organization organization);

  List<OrganizationDto> toResponse(List<Organization> organization);

  CreateOrganizationMembershipRequest toRequest(PostOrganizationMemberDto dto);

  @Mapping(target = "memberId", source = "memberId")
  @Mapping(target = "role", source = "dto.role")
  UpdateOrganizationMembershipRequest toRequest(PutOrganizationMemberDto dto, UUID memberId);

  @Mapping(target = "member", source = "userId")
  OrganizationMemberDto toResponse(OrganizationMembershipResponse response);
}
