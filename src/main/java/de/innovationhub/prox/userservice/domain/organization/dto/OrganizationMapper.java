package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.Organization;
import org.mapstruct.Mapper;

@Mapper
public interface OrganizationMapper {
  GetOrganizationResponse organizationToGetDto(Organization organization);
  PostOrganizationResponse organizationToPostDto(Organization organization);
  //Organization organizationPostDtoToOrganization(OrganizationPostDto postDto);
}
