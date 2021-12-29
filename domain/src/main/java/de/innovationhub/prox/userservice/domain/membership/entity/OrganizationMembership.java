package de.innovationhub.prox.userservice.domain.membership.entity;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.membership.vo.OrganizationRole;
import java.util.UUID;
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

  /** Identifier */
  private final UUID id;

  /** Name of the org */
  private final Organization organization;

  private final User user;

  private OrganizationRole role;

  public OrganizationMembership(UUID id,
      Organization organization, User user,
      OrganizationRole role) {
    this.id = id;
    this.organization = organization;
    this.user = user;
    this.role = role;
  }
}
