package de.innovationhub.prox.userservice.domain.user;

import de.innovationhub.prox.userservice.domain.user.dto.UserGetDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "cdi")
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserGetDto toGetDto(User user);
}
