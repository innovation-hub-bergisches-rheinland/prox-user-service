package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import java.util.UUID;

public record PutOrganizationMemberDto(
    @JsonProperty("role") OrganizationRole role
) {
}
