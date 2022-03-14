package de.innovationhub.prox.userservice.organization.dto.response;

import java.util.UUID;

public record ViewOrganizationDto(UUID id, String name, ViewOrganizationProfileDto profile) {}
