package de.innovationhub.prox.userservice.user.entity;

import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import io.smallrye.common.constraint.Nullable;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

@Data
public class User {
  private final UUID id;
  private final String name;
  private final String email;
  @Nullable private UserProfile profile;

  public User(UUID id, String name, String email) {
    Objects.requireNonNull(id);
    Objects.requireNonNull(id);
    Objects.requireNonNull(id);

    this.id = id;
    this.name = name;
    this.email = email;
  }
}
