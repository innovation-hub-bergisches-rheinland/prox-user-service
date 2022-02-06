package de.innovationhub.prox.userservice.organization.dto;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface OrganizationMapper {
  OrganizationDto toDto(Organization organization);
}
