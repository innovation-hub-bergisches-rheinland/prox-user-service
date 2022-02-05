package de.innovationhub.prox.userservice.organization.dto.response;

import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import java.util.UUID;

public record OrganizationMembershipResponse(
    UUID userId,
    OrganizationRole role
) {

}
