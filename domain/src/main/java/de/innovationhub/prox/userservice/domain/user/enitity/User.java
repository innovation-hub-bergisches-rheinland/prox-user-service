package de.innovationhub.prox.userservice.domain.user.enitity;

import java.util.UUID;
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

  public User(@NonNull String principal) {
    this.principal = principal;
  }
}
