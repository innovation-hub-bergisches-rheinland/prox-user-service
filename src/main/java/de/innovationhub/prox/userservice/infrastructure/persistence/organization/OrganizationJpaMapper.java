package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.user.UserId;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface OrganizationJpaMapper {
  OrganizationJpaMapper INSTANCE = Mappers.getMapper(OrganizationJpaMapper.class);

  @Mapping(target = "id", source = "id.id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "owner", source = "owner.id")
  OrganizationJpaEntity toPersistence(Organization organization);

  @Mapping(target = "id.id", source = "id")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "owner.id", source = "owner")
  Organization toDomain(OrganizationJpaEntity entity);

  default Map<UserId, OrganizationMembership> toDomain(
      Set<OrganizationMembershipJpaEmbeddable> persistence) {
    return persistence.stream()
        .collect(Collectors.toMap(
            k -> new UserId(k.getUserId()),
            v -> new OrganizationMembership(v.getRole())
        ));
  }

  default Set<OrganizationMembershipJpaEmbeddable> toPersistence(Map<UserId, OrganizationMembership> domain) {
    return domain.entrySet()
        .stream()
        .map(e -> new OrganizationMembershipJpaEmbeddable(e.getKey().id(), e.getValue().getRole()))
        .collect(Collectors.toSet());
  }
}
