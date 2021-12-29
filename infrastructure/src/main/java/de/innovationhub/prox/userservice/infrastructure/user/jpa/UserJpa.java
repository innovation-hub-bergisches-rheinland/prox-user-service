package de.innovationhub.prox.userservice.infrastructure.user.jpa;

import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationMembershipJpa;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class UserJpa {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id = UUID.randomUUID();

  @NaturalId
  @Column(name = "principal", updatable = false, nullable = false, unique = true)
  private String principal;

  /*@OneToMany(mappedBy = "user")
  private Set<OrganizationMembershipJpa> memberships;*/
}
