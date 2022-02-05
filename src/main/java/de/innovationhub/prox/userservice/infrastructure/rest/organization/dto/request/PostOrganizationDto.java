package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostOrganizationDto(
    @JsonProperty("name") String name
) {

}
