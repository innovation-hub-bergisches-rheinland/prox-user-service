package de.innovationhub.prox.userservice.application.organization.dto.request;

import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.UUID;

public record UpdateOrganizationMembershipRequest(
    UUID memberId,
    OrganizationRole role
) {

}
