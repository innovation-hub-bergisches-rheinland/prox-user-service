package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.user.User;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
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
public class Organization {

  /** Identifier */
  private final UUID id;

  /** Name of the org */
  private String name;

  /** Members of the org */
  @Setter(AccessLevel.NONE)
  private Map<User, OrganizationMembership> members;

  public Organization(String name, User owner) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(owner);
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.id = UUID.randomUUID();
    this.name = name;
    this.members = Map.of(owner, new OrganizationMembership(OrganizationRole.OWNER));
  }

  public void setName(String name) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.name = name;
  }
}
