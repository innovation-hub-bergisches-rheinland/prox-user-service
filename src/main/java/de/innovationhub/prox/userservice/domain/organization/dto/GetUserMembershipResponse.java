package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.user.dto.GetUserResponse;

public record GetUserMembershipResponse(
  GetUserResponse user, MembershipType type
) {}
