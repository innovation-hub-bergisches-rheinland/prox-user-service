package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;

public record GetOrganizationMembershipResponse(
  GetOrganizationResponse organization, MembershipType type
) {}
