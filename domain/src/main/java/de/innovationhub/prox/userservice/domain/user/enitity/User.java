package de.innovationhub.prox.userservice.domain.user.enitity;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationRole;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
  /**
   * A unique and constant identifier of the user can be a username, id or email address.
   * However, it cannot change over time!
   */
  private final String principal;

  @Setter(AccessLevel.NONE)
  private Map<Organization, OrganizationMembership> memberships = new HashMap<>();

  public User(@NonNull String principal) {
    this.principal = principal;
  }

  public void addMembership(@NonNull Organization org, @NonNull OrganizationMembership membership) {
    if(memberships.containsKey(org)) {
      throw new RuntimeException("User is already a member of the organization, consider updating the membership");
    }
    this.memberships.put(org, membership);
  }

  // Technically the same as addMembership, but for clarity seperated methods
  public void updateMembership(@NonNull Organization org, @NonNull OrganizationMembership membership) {
    if(!memberships.containsKey(org)) {
      throw new RuntimeException("User is not a member of the organization, consider adding the membership");
    }
    this.memberships.put(org, membership);
  }

  public void removeMembership(@NonNull Organization org) {
    memberships.remove(org);
  }
}
