package de.innovationhub.prox.userservice.domain.organization.entity;

import de.innovationhub.prox.userservice.domain.core.user.entity.ProxUser;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
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
  private final OrganizationId id;

  /** Name of the org */
  private String name;

  private final ProxUser owner;

  @Setter(AccessLevel.NONE)
  private final Map<ProxUser, OrganizationMembership> members = new HashMap<>();

  public Organization(@NonNull OrganizationId id, @NonNull String name, @NonNull ProxUser owner) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }

    this.id = id;
    this.name = name;
    this.owner = owner;
  }

  public void addMember(ProxUser member, OrganizationMembership role) {
    if(members.containsKey(member)) throw new UnsupportedOperationException("Cannot overwrite membership");
    this.members.put(member, role);
  }

  public void removeMember(ProxUser user) {
    var membership = members.get(user);
    if(membership == null) {
      throw new UnsupportedOperationException("Representative not a member of the organization");
    }
    this.members.remove(user);
  }

  public void setName(@NonNull String name) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.name = name;
  }

  public record OrganizationId(UUID id) {}
}
