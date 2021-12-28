package de.innovationhub.prox.userservice.application.user.mapper;

import de.innovationhub.prox.userservice.application.user.message.response.ReadUserResponse;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  ReadUserResponse toGetDto(User user);
  User toDomain(UserJpa userJpa);
}
