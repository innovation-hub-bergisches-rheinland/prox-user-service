package de.innovationhub.prox.userservice.infrastructure.iam.mapper;

import de.innovationhub.prox.userservice.domain.user.entity.ProxUser;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi", imports = UUID.class)
public interface UserMapper {
  @Mapping(target = "id", expression = "java( UUID.fromString(representation.getId()) )")
  ProxUser toDomain(UserRepresentation representation);

  Set<ProxUser> toSet(Stream<UserRepresentation> users);
}
