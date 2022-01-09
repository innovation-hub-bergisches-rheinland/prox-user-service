package de.innovationhub.prox.userservice.domain.organization.entity;

import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents an Organization in PROX, examples are companies, laboratories, institutes or other
 * organizations
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
