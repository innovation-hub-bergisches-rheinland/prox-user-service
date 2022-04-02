package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
  Optional<UserSearchResponseDto> findById(UUID id);

  boolean existsById(UUID id);

  Iterable<UserSearchResponseDto> search(String query);

  List<ViewOrganizationDto> findOrganizationsOfAuthenticatedUser();
}
