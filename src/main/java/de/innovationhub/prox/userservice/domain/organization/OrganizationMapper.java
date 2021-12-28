package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationGetDto;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationPostDto;
import de.innovationhub.prox.userservice.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface OrganizationMapper {
  OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

  OrganizationGetDto toGetDto(Organization organization);

  @Mapping(source = "dto.name", target = "name")
  @Mapping(source = "user", target = "owner")
  Organization toDomainObject(OrganizationPostDto dto, User owner);
}
