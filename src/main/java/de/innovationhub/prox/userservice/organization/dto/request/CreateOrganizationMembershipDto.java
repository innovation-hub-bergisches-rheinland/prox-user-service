package de.innovationhub.prox.userservice.organization.dto.request;

import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.user.constraints.IsValidUserId;
import java.util.UUID;
import javax.validation.constraints.NotNull;

public record CreateOrganizationMembershipDto(
    @IsValidUserId @NotNull UUID member, @NotNull OrganizationRole role) {}
