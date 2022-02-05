package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.UUID;

public record PutOrganizationMemberDto(
    @JsonProperty("role") OrganizationRole role
) {
}
