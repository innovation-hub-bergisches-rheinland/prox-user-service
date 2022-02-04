package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record OrganizationDto(
    @JsonProperty("id") UUID id,
    @JsonProperty("name") String name,
    @JsonProperty("owner") UUID owner
    ) {

}
