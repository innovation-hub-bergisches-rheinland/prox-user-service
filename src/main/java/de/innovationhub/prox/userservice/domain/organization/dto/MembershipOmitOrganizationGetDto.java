package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.user.dto.UserGetDto;

public record MembershipOmitOrganizationGetDto(
  UserGetDto user, MembershipType type
) {}
