package de.innovationhub.prox.userservice.infrastructure.rest.organization.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostOrganizationJsonRequest(
    @JsonProperty("name") String name,
    @JsonProperty("owner") String owner
) {

}
