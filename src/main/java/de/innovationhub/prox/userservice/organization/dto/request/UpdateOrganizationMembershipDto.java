package de.innovationhub.prox.userservice.organization.dto.request;

import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import javax.validation.constraints.NotNull;

public record UpdateOrganizationMembershipDto(@NotNull OrganizationRole role) {}
