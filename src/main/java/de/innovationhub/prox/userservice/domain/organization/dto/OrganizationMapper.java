package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.Organization;
import org.mapstruct.Mapper;

@Mapper
public interface OrganizationMapper {
  OrganizationGetDto organizationToGetDto(Organization organization);
  //Organization organizationPostDtoToOrganization(OrganizationPostDto postDto);
}
