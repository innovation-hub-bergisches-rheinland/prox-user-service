package de.innovationhub.prox.userservice.user.entity;

import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(
    componentModel = "cdi",
    imports = {UUID.class})
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(target = "id", expression = "java( UUID.fromString( representation.getId()) )")
  @Mapping(target = "email", source = "email")
  @Mapping(target = "name", source = ".", qualifiedByName = "parseName")
  User toEntity(UserRepresentation representation);

  @Mapping(target = "id")
  @Mapping(target = "name")
  UserSearchResponseDto toDto(User user);

  Set<UserSearchResponseDto> toDtoSet(Stream<User> users);

  @Named("parseName")
  default String parseName(UserRepresentation representation) {
    var firstName = representation.getFirstName();
    var lastName = representation.getLastName();

    // Defaulting to username
    if (firstName == null && lastName == null) {
      return representation.getUsername();
    }

    return (firstName == null ? "" : firstName) + " " + (lastName == null ? "" : lastName);
  }
}
