package de.innovationhub.prox.userservice.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.UUID;

public record OrganizationDto(
    @JsonProperty(value = "id", access = Access.READ_ONLY) UUID id,
    @JsonProperty(value = "name") String name
) {

}
