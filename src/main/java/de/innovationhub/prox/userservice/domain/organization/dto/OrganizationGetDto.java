package de.innovationhub.prox.userservice.domain.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record OrganizationGetDto(
  @JsonProperty("id") UUID id, @JsonProperty("name") String name
) {}
