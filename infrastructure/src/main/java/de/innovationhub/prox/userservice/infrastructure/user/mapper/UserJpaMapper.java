package de.innovationhub.prox.userservice.infrastructure.user.mapper;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface UserJpaMapper {
  @Mapping(target = "id", source = "id")
  UserJpa toPersistence(User user);

  @Mapping(target = "id", source = "id")
  User toDomain(UserJpa userJpa);
}
