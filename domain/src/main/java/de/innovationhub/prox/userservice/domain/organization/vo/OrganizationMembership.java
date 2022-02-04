package de.innovationhub.prox.userservice.domain.organization.vo;

import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an Organization membership in PROX
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrganizationMembership {
  @Setter(AccessLevel.NONE)
  private OrganizationRole role;

  public OrganizationMembership(
      OrganizationRole role) {
    this.role = role;
  }

  public void setRole(OrganizationRole role) {
    this.role = role;
  }
}
