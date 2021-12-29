package de.innovationhub.prox.userservice.application.user.mapper;

import de.innovationhub.prox.userservice.application.user.message.dto.UserDTO;
import de.innovationhub.prox.userservice.application.user.message.response.ReadUserResponse;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "principal", source = "principal")
  UserDTO toDto(User user);

  @Mapping(target = "user.principal", source = "dto.principal")
  ReadUserResponse toReadResponse(UserDTO dto);
}
