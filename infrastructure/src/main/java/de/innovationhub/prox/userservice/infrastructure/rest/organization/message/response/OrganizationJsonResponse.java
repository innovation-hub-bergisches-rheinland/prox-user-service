package de.innovationhub.prox.userservice.infrastructure.rest.organization.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record OrganizationJsonResponse(
    @JsonProperty("id") UUID id,
    @JsonProperty("name") String name,
    @JsonProperty("owner") UUID owner
    ) {

}
