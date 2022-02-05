package de.innovationhub.prox.userservice.organization.dto.request;

import java.util.UUID;

public record DeleteOrganizationMembershipRequest(
    UUID userId
) {

}
