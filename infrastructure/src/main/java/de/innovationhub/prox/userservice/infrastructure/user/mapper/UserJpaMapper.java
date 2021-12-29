package de.innovationhub.prox.userservice.infrastructure.user.mapper;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.infrastructure.organization.mapper.OrganizationJpaMapper;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import java.util.UUID;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi", imports = {UUID.class}, uses = {OrganizationJpaMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserJpaMapper {
  UserJpaMapper INSTANCE = Mappers.getMapper(UserJpaMapper.class);

  @Mapping(target = "id", expression = "java( UUID.randomUUID() )")
  @Mapping(target = "principal", source = "principal")
  UserJpa createJpaEntity(User user);

  @Mapping(target = "principal", source = "principal")
  User toDomainEntity(UserJpa userJpa);
}
