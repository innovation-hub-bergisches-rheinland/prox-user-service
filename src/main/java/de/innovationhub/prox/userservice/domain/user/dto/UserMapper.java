package de.innovationhub.prox.userservice.domain.user.dto;

import de.innovationhub.prox.userservice.domain.user.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
  GetUserResponse userToUserGetDto(User user);
}
