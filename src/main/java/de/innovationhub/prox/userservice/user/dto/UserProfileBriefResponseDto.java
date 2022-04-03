package de.innovationhub.prox.userservice.user.dto;

import java.util.UUID;

public record UserProfileBriefResponseDto(UUID id, String name, String mainSubject) {}
