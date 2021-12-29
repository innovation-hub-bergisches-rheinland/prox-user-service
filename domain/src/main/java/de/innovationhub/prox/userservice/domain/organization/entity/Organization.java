package de.innovationhub.prox.userservice.domain.organization.entity;

import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;

/**
 * Represents an Organization in PROX, examples are companies, laboratories, institutes or other
 * organizations
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Organization {

  /** Identifier */
  private final UUID id;

  /** Name of the org */
  private String name;

  public Organization(@NonNull UUID id, @NonNull String name) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }

    this.id = id;
    this.name = name;
  }

  public void setName(@NonNull String name) {
    if (name.trim().length() > 255 || name.trim().length() <= 0) {
      throw new IllegalArgumentException("Name length must be between 0 and 255");
    }
    this.name = name;
  }
}
