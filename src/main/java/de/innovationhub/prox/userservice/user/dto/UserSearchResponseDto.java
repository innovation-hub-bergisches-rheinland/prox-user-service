package de.innovationhub.prox.userservice.user.dto;

import java.util.Optional;
import java.util.UUID;
import javax.swing.text.html.Option;
import lombok.Value;

public record UserSearchResponseDto(
  UUID id,
  String name,
  Optional<UserProfileBriefResponseDto> profile
) { }
