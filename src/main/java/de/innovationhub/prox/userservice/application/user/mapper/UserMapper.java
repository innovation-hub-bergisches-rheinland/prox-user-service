package de.innovationhub.prox.userservice.application.user.mapper;

import de.innovationhub.prox.userservice.application.user.message.dto.UserDTO;
import de.innovationhub.prox.userservice.application.user.message.response.ReadUserResponse;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", source = "id")
  UserDTO toDto(User user);

  @Mapping(target = "id", source = "id")
  ReadUserResponse toReadResponse(UserDTO dto);
}
