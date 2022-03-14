package de.innovationhub.prox.userservice.organization.entity.profile;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
@ToString
public class Branch {

  @NotBlank
  @Size(max = 255)
  private String name;

  public Branch(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Branch name cannot be null or empty");
    }
    this.name = name;
  }
}
