package de.innovationhub.prox.userservice.application.organization.mapper;

import de.innovationhub.prox.userservice.application.organization.message.response.ReadOrganizationResponse;
import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.Map;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = { UUID.class, Map.class })
public interface OrganizationMapper {
  OrganizationMapper INSTANCE = Mappers.getMapper(OrganizationMapper.class);

  ReadOrganizationResponse toGetDto(Organization organization);

  @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "members", expression = "java( Map.of(owner, new OrganizationMembership(OrganizationRole.OWNER)) )")
  Organization toDomainObject(CreateOrganizationRequest dto, User owner);


  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  OrganizationJpa toPersistenceEntity(Organization organization);
}
