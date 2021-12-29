package de.innovationhub.prox.userservice.infrastructure.organization.mapper;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = { UUID.class })
public interface OrganizationJpaMapper {
  OrganizationJpaMapper INSTANCE = Mappers.getMapper(OrganizationJpaMapper.class);

  @Mapping(target = "id", source = "id", defaultExpression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "name")
  OrganizationJpa toPersistence(Organization organization);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  Organization toDomain(OrganizationJpa organization);
}
