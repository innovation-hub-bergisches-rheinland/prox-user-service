package de.innovationhub.prox.userservice.domain.organization.dto;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import javax.validation.constraints.NotNull;

public record GetOrganizationMembershipResponse(
  @NotNull GetOrganizationResponse organization, @NotNull MembershipType type
) {}
