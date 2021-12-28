package de.innovationhub.prox.userservice.domain.organization.entity;

import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import de.innovationhub.prox.userservice.domain.user.enitity.User;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

/**
 * Represents an Organization in PROX, examples are companies, laboratories, institutes or other
 * organizations
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Organization {

  /** Identifier */
  private final UUID id;

  /** Name of the org */
  private String name;

  /** Members of the org */
  @Setter(AccessLevel.NONE)
  @Singular
  private Map<User, OrganizationMembership> members;

  public Organization(UUID id, String name, Map<User, OrganizationMembership> members) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(name);
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }

    Objects.requireNonNull(members);
    if (members.size() == 0 || !members.entrySet().stream().anyMatch(m -> m.getKey() != null && m.getValue().role() == OrganizationRole.OWNER)) {
      throw new IllegalArgumentException("Must have a valid owner");
    }

    this.id = id;
    this.name = name;
    this.members = members;
  }

  public void setName(String name) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.name = name;
  }
}
