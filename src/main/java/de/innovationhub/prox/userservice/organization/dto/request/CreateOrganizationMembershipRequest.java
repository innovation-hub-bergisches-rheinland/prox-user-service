package de.innovationhub.prox.userservice.organization.dto.request;

import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import java.util.UUID;

public record CreateOrganizationMembershipRequest(
    UUID userId,
    OrganizationRole role
) {

}
