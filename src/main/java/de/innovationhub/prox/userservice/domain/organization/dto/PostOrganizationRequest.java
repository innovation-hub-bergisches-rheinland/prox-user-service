package de.innovationhub.prox.userservice.domain.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public record PostOrganizationRequest(
  @JsonProperty("name") @NotNull String name
) {}
