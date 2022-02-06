package de.innovationhub.prox.userservice.organization.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateOrganizationDto(@NotNull @NotBlank String name) {}
