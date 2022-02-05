package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.UUID;

public record OrganizationMemberDto(
    @JsonProperty(value = "userId", required = true) UUID userId,
    @JsonProperty(value = "role", required = true) OrganizationRole role
) {

}
