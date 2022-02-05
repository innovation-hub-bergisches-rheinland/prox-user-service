package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.UUID;

public record PostOrganizationMemberDto(
    @JsonProperty(value = "userId", required = true) UUID userId,
    @JsonProperty("role") OrganizationRole role
    ) {
}
