package de.innovationhub.prox.userservice.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public record OrganizationDto(
    @JsonProperty(value = "id", access = Access.READ_ONLY) UUID id,
    @NotBlank
    @NotEmpty
    @JsonProperty(value = "name") String name
) {

}
