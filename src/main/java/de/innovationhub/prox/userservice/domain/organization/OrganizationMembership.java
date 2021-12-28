package de.innovationhub.prox.userservice.domain.organization;

/**
 * Holds information about the organization membership. At the moment the
 * only necessary information is a role within the organization. Other thinkable applications are
 * "invitation status" for memberships that must be accepted first.
 */
public record OrganizationMembership(OrganizationRole role) {}
