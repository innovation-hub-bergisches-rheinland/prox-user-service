package de.innovationhub.prox.userservice.domain.user;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class UserJpa {

  @Id
  @Column(name = "id")
  @Setter(AccessLevel.NONE)
  private UUID id;

  public UserJpa(UUID id) {
    this.id = id;
  }
}
