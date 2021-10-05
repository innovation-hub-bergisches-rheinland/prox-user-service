package de.innovationhub.prox.userservice.domain.organization;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrganizationDto(
    @JsonProperty("name")
    String name
) {

}
