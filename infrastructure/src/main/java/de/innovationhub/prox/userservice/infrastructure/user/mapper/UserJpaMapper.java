package de.innovationhub.prox.userservice.infrastructure.user.mapper;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.infrastructure.organization.mapper.OrganizationJpaMapper;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UserJpaMapper {
  UserJpaMapper INSTANCE = Mappers.getMapper(UserJpaMapper.class);

  @Mapping(target = "id", source = "id")
  UserJpa toPersistence(User user);

  @Mapping(target = "id", source = "id")
  User toDomain(UserJpa userJpa);
}
