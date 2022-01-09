package de.innovationhub.prox.userservice.application.organization.message.response;

import java.util.UUID;

public record OrganizationResponse(UUID id, String name, String ownerPrincipal) { }
