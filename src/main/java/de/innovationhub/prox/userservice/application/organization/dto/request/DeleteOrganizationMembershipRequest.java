package de.innovationhub.prox.userservice.application.organization.dto.request;

import java.util.UUID;

public record DeleteOrganizationMembershipRequest(
    UUID userId
) {

}
