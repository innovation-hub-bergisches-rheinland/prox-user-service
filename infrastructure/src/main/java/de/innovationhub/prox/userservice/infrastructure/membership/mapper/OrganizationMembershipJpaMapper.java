package de.innovationhub.prox.userservice.infrastructure.membership.mapper;

import de.innovationhub.prox.userservice.domain.membership.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.infrastructure.membership.jpa.OrganizationMembershipJpa;
import de.innovationhub.prox.userservice.infrastructure.organization.mapper.OrganizationJpaMapper;
import de.innovationhub.prox.userservice.infrastructure.user.mapper.UserJpaMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", uses = {OrganizationJpaMapper.class, UserJpaMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrganizationMembershipJpaMapper {
  @Mapping(target = "id", source = "id")
  @Mapping(target = "role", source = "role")
  @Mapping(target = "organization", source = "organization")
  @Mapping(target = "user", source = "user")
  OrganizationMembershipJpa toPersistence(OrganizationMembership membership);

  @Mapping(target = "id", source = "id")
  @Mapping(target = "role", source = "role")
  @Mapping(target = "organization", source = "organization")
  @Mapping(target = "user", source = "user")
  OrganizationMembership toDomain(OrganizationMembershipJpa jpa);
}
