package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.user.UserJpa;
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
 * Represents an Organization in PROX, examples are companies, laboratories, institutes or other
 * organizations
 */
@Entity
@Table(name = "organizations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class OrganizationJpa {
  /** Identifier */
  @Id
  @Column(name = "id")
  @Setter(AccessLevel.NONE)
  private UUID id;

  /** Name of the org */
  @Column(name = "name", nullable = false)
  private String name;

  /** Owners of the org */
  @ManyToMany
  @JoinTable(
      name = "organization_owners",
      joinColumns = @JoinColumn(name = "organization_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  @Setter(AccessLevel.NONE)
  private Set<UserJpa> owners;

  /** Members of the org */
  @ManyToMany
  @JoinTable(
      name = "organization_members",
      joinColumns = @JoinColumn(name = "organization_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id"))
  @Setter(AccessLevel.NONE)
  private Set<UserJpa> members;
}
