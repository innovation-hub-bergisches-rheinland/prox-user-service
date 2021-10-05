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

@Entity
@Table(name = "organizations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Organization {
  @Id
  @Column(name = "id")
  @Setter(AccessLevel.NONE)
  private UUID id;
  
  @Column(name = "name", nullable = false)
  private String name;

  @ManyToMany
  @JoinTable(
      name = "organization_owners",
      joinColumns = @JoinColumn(name = "organization_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  @Setter(AccessLevel.NONE)
  private Set<User> owners;

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
    if(name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.id = UUID.randomUUID();
    this.name = name;
    this.owners = new HashSet<>(Collections.singletonList(owner));
    this.members = new HashSet<>(Collections.singletonList(owner));
  }

  public void addOwner(User user) {
    if(!members.contains(user)) {
      throw new RuntimeException("Not a member of the organization");
    }
    this.owners.add(user);
  }

  public void removeOwner(User user) {
    if(owners.contains(user)) {
      if(owners.size() == 1) {
        throw new RuntimeException("Cannot remove last owner");
      }
      this.owners.remove(user);
    }
  }

  public void addMember(User user) {
    this.members.add(user);
  }

  public void removeMember(User user) {
    if(owners.contains(user)) {
      throw new RuntimeException("Cannot remove owner from members");
    }
    this.members.remove(user);
  }

  public void setName(String name) {
    if(name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.name = name;
  }
}
