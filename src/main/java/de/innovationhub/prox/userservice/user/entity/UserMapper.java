package de.innovationhub.prox.userservice.user.entity;

import de.innovationhub.prox.userservice.user.dto.UserProfileRequestDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.entity.profile.ContactInformation;
import de.innovationhub.prox.userservice.user.entity.profile.Publication;
import de.innovationhub.prox.userservice.user.entity.profile.ResearchSubject;
import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import io.smallrye.common.constraint.Nullable;
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

  UserProfileResponseDto toDto(UserProfile userProfile);

  default @Nullable String toString(@Nullable ResearchSubject researchSubject) {
    return researchSubject == null ? null : researchSubject.getSubject();
  }

  default @Nullable ResearchSubject createResearchSubjectFroMString(@Nullable String s) {
    if (s != null && !s.isBlank()) {
      return new ResearchSubject(s);
    }
    return null;
  }

  default @Nullable String toString(@Nullable Publication publication) {
    return publication == null ? null : publication.getPublication();
  }

  default @Nullable Publication createPublicationFromString(@Nullable String s) {
    if (s != null && !s.isBlank()) {
      return new Publication(s);
    }
    return null;
  }

  UserProfileResponseDto.ContactInformationResponseDto toDto(ContactInformation contactInformation);

  @Mapping(target = "userId", source = "userId")
  @Mapping(target = ".", source = "request")
  UserProfile toEntity(UUID userId, UserProfileRequestDto request);
}
