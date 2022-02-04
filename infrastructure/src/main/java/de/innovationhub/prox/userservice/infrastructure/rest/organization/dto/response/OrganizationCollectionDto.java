package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

public record OrganizationCollectionDto(
    @JsonProperty("organizations") Set<OrganizationDto> organizations
) { }
