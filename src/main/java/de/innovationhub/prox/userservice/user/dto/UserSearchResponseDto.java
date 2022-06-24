package de.innovationhub.prox.userservice.user.dto;

import java.util.Optional;
import java.util.UUID;

public record UserSearchResponseDto(
    UUID id, String name, Optional<UserProfileBriefResponseDto> profile) {}
