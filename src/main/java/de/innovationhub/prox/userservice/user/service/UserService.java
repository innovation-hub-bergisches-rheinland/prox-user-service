package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
  Optional<UserResponseDto> findById(UUID id);

  boolean existsById(UUID id);

  Iterable<UserResponseDto> search(String query);

  List<ViewOrganizationDto> findOrganizationsOfAuthenticatedUser();
}
