package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.user.User;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
   * Members of the org
   */
  @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
  @Setter(AccessLevel.NONE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Membership> members;

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
    var membership = new Membership(owner, this, MembershipType.OWNER);
    this.members = new HashSet<>(Collections.singletonList(membership));
  }

  /**
   * Adds member to the org
   * @param user user which should become a member
   */
  public void addMember(User user) {
    this.members.add(new Membership(user, this));
  }

  /**
   * Removes a member from the org
   * @param member member to remove
   * @throws RuntimeException if member was the last owner
   */
  public void removeMember(User member) {
    var optMember = members
      .stream()
      .filter(m -> m.getUser().equals(member))
      .findFirst();

    if (optMember.isPresent()) {
      if (
        !members
          .stream()
          .anyMatch(m ->
            !m.getUser().equals(member) && m.getType() == MembershipType.OWNER
          )
      ) {
        throw new RuntimeException("Cannot remove last owner");
      }
      this.members.remove(optMember.get());
    }
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
