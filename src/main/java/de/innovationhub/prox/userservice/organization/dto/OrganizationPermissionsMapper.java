package de.innovationhub.prox.userservice.organization.dto;

import de.innovationhub.prox.userservice.organization.dto.response.OrganizationPermissionsDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.UUID;
import javax.inject.Inject;

public abstract class OrganizationPermissionsMapper implements OrganizationMapper {
  @Inject SecurityIdentity securityIdentity;

  public OrganizationPermissionsDto map(Organization organization) {
    var id = UUID.fromString(securityIdentity.getPrincipal().getName());
    var membership = organization.getMembers().get(id);
    boolean canViewMembers = membership != null || organization.getOwner().equals(id);
    boolean canEdit = organization.getOwner().equals(id);
    return new OrganizationPermissionsDto(canEdit, canViewMembers);
  }
}
