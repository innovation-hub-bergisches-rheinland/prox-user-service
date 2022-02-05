package de.innovationhub.prox.userservice.infrastructure.rest.organization.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import java.util.UUID;

public record OrganizationMemberDto(
    @JsonProperty(value = "member", required = true) UUID member,
    @JsonProperty(value = "role", required = true) OrganizationRole role
) {

}
