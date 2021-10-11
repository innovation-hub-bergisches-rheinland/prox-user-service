package de.innovationhub.prox.userservice.domain.organization;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class MembershipId implements Serializable {

  private UUID user;
  private UUID organization;
}
