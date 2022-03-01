package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import java.util.Optional;
import java.util.UUID;

public interface UserIdentityService {
  Optional<UserResponseDto> findById(UUID id);

  boolean existsById(UUID id);

  Iterable<UserResponseDto> search(String query);
}
