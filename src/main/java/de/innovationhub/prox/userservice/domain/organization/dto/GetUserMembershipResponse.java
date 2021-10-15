package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.user.dto.GetUserResponse;
import javax.validation.constraints.NotNull;

public record GetUserMembershipResponse(
  @NotNull GetUserResponse user, @NotNull MembershipType type
) {}
