package de.innovationhub.prox.userservice.organization.entity.profile;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Embeddable
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quarter {
  @NotBlank
  @Size(max = 255)
  private String location;

  public Quarter(String location) {
    this.setLocation(location);
  }

  public void setLocation(String location) {
    if (location == null || location.isBlank() || location.trim().length() > 255) {
      throw new IllegalArgumentException("Location cannot be null or blank");
    } else if (location.trim().length() > 255) {
      throw new IllegalArgumentException("Location cannot be longer than 255 characters");
    }
    this.location = location.trim();
  }
}
