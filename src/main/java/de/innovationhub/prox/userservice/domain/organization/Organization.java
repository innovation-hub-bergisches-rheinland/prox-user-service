package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.user.User;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an Organization in PROX, examples are
 * companies, laboratories, institutes or other organizations
 */
@Entity
@Table(name = "organizations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Organization {

  /**
   * Identifier
   */
  @Id
  @Column(name = "id")
  @Setter(AccessLevel.NONE)
  private UUID id;

  /**
   * Name of the org
   */
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * Owners of the org
   */
  @ManyToMany
  @JoinTable(
    name = "organization_owners",
    joinColumns = @JoinColumn(name = "organization_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @Setter(AccessLevel.NONE)
  private Set<User> owners;

  /**
   * Members of the org
   */
  @ManyToMany
  @JoinTable(
    name = "organization_members",
    joinColumns = @JoinColumn(name = "organization_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @Setter(AccessLevel.NONE)
  private Set<User> members;

  public Organization(String name, User owner) {
    Objects.requireNonNull(name);
    Objects.requireNonNull(owner);
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException(
        "Name length must be between 0 and 255"
      );
    }
    this.id = UUID.randomUUID();
    this.name = name;
    this.owners = new HashSet<>(Collections.singletonList(owner));
    this.members = new HashSet<>(Collections.singletonList(owner));
  }

  /**
   * Adds an owner to the company
   * @param member member of the organization which should become an owner
   * @throws RuntimeException if provided user is not a member of the org
   */
  public void addOwner(User member) {
    if (!members.contains(member)) {
      throw new RuntimeException("Not a member of the organization");
    }
    this.owners.add(member);
  }

  /**
   * Removes an owner of the org
   * @param owner owner of the organization which should be removed
   * @throws RuntimeException if the owner is the last one present in the org
   */
  public void removeOwner(User owner) {
    if (owners.contains(owner)) {
      if (owners.size() == 1) {
        throw new RuntimeException("Cannot remove last owner");
      }
      this.owners.remove(owner);
    }
  }

  /**
   * Adds member to the org
   * @param user user which should become a member
   */
  public void addMember(User user) {
    this.members.add(user);
  }

  /**
   * Removes a member from the org
   * @param member member to remove
   * @throws RuntimeException if member is a owner
   */
  public void removeMember(User member) {
    if (owners.contains(member)) {
      throw new RuntimeException("Cannot remove owner from members");
    }
    this.members.remove(member);
  }

  public void setName(String name) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException(
        "Name length must be between 0 and 255"
      );
    }
    this.name = name;
  }
}
