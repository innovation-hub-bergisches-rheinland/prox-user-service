package de.innovationhub.prox.userservice.infrastructure.user.jpa;

import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

  /*@OneToMany(cascade = {
      CascadeType.PERSIST,
      CascadeType.MERGE,
      CascadeType.REFRESH
  })
  @JoinColumn(name="user_id", updatable = false, nullable = false)
  private Set<OrganizationMembershipJpa> organizations;*/
}
