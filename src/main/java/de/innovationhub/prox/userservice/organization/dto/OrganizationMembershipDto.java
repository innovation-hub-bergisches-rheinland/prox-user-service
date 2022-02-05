package de.innovationhub.prox.userservice.organization.dto;

import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import java.util.UUID;

public record OrganizationMembershipDto(
    UUID member,
    OrganizationRole role
) {

}
