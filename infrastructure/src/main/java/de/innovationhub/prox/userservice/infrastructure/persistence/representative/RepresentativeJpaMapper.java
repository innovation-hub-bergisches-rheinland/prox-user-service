package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import de.innovationhub.prox.userservice.domain.representative.enitity.Representative;
import de.innovationhub.prox.userservice.infrastructure.persistence.organization.OrganizationJpaMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RepresentativeJpaMapper {
  RepresentativeJpaMapper INSTANCE = Mappers.getMapper(RepresentativeJpaMapper.class);

  @Mapping(target = "id.id", source = "id")
  @Mapping(target = "user.id", source = "owner")
  @Mapping(target = "name", source = "name")
  Representative toDomain(RepresentativeJpaEntity jpaEntity);

  @Mapping(target = "id", source = "id.id")
  @Mapping(target = "owner", source = "user.id")
  @Mapping(target = "name", source = "name")
  RepresentativeJpaEntity toPersistence(Representative domain);
}
