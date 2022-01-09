package de.innovationhub.prox.userservice.domain.representative.enitity;

import de.innovationhub.prox.userservice.domain.core.user.entity.ProxUser;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * A Representative is a special type of user in PROX which has a supervisor function.
 * Can be a company employee, lecturer, professor or similar
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Representative {
  private final RepresentativeId id;
  private final ProxUser user;
  private String name;

  public Representative(RepresentativeId id,
      ProxUser user, String name) {
    this.id = id;
    this.user = user;
    this.name = name;
  }

  public record RepresentativeId(UUID id) {}
}
