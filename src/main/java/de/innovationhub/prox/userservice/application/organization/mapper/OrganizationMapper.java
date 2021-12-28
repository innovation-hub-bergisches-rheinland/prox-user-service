package de.innovationhub.prox.userservice.application.organization.mapper;

import de.innovationhub.prox.userservice.application.organization.message.dto.OrganizationDTO;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.ReadOrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import java.util.Map;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = { UUID.class, Map.class })
public interface OrganizationMapper {
  OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  OrganizationDTO toDto(Organization organization);

  @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "name")
  OrganizationDTO toDto(CreateOrganizationRequest request);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  ReadOrganizationResponse toReadResponse(OrganizationDTO dto);
}
