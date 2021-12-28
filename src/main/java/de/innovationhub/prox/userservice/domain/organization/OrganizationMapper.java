package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationGetDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface OrganizationMapper {
  OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

  OrganizationGetDto toGetDto(Organization organization);
}
