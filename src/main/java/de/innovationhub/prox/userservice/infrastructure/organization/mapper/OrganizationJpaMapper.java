package de.innovationhub.prox.userservice.infrastructure.organization.mapper;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", imports = { UUID.class })
public interface OrganizationJpaMapper {
  @Mapping(target = "id", source = "id", defaultExpression = "java( UUID.randomUUID() )")
  @Mapping(target = "name", source = "name")
  OrganizationJpa toPersistence(Organization organization);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "name", source = "name")
  Organization toDomain(OrganizationJpa organization);
}
