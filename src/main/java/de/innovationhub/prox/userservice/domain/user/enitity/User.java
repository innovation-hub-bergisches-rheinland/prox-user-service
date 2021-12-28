package de.innovationhub.prox.userservice.domain.user.enitity;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
  private final UUID id;

  public User(UUID id) {
    this.id = id;
  }
}
