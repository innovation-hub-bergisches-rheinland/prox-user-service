package de.innovationhub.prox.userservice.domain.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import javax.validation.constraints.NotNull;

public record PostOrganizationResponse(
  @JsonProperty("id") @NotNull UUID id, @JsonProperty(
    "name"
  ) @NotNull String name
) {}
