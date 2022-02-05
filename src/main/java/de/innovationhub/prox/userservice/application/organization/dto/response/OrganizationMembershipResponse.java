package de.innovationhub.prox.userservice.application.organization.dto.response;

import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.UUID;

public record OrganizationMembershipResponse(
    UUID userId,
    OrganizationRole role
) {

}
