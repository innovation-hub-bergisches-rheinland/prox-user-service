package de.innovationhub.prox.userservice.domain.organization;

import java.io.Serializable;
import java.util.UUID;
import lombok.Data;

@Data
public class OrganizationMembershipId implements Serializable {
  private UUID organization;
  private UUID user;
}
