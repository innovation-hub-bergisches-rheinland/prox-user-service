package de.innovationhub.prox.userservice.domain.user;

import de.innovationhub.prox.userservice.domain.organization.Membership;
import java.util.Collections;
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

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class User {

  @Id
  @Column(name = "id")
  @Setter(AccessLevel.NONE)
  private UUID id;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @Setter(AccessLevel.NONE)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Set<Membership> members = Collections.emptySet();

  public User(UUID id) {
    this.id = id;
  }
}
