package de.innovationhub.prox.userservice.organization.dto;

import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "cdi",
    imports = {UUID.class})
public interface OrganizationMapper {
  ViewOrganizationDto toDto(Organization organization);

  @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "owner", source = "owner")
  Organization createFromDto(CreateOrganizationDto dto, UUID owner);
}
