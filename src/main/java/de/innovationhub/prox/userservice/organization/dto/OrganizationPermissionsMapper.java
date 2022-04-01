package de.innovationhub.prox.userservice.organization.dto;

import de.innovationhub.prox.userservice.organization.dto.response.OrganizationPermissionsDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.UUID;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
@Slf4j
public abstract class OrganizationPermissionsMapper {
  @Inject SecurityIdentity securityIdentity;

  public OrganizationPermissionsDto map(Organization organization) {
    boolean canViewMembers = false;
    boolean canEdit = false;
    if (!securityIdentity.isAnonymous()) {
      var principal = securityIdentity.getPrincipal().getName();
      if (principal != null) {
        try {
          var id = UUID.fromString(securityIdentity.getPrincipal().getName());
          var membership = organization.getMembers().get(id);

          canViewMembers = membership != null;
          canEdit = membership.getRole() == OrganizationRole.ADMIN;
        } catch (IllegalArgumentException e) {
          log.error("Principal not an uuid", e);
        }
      }
    }
    return new OrganizationPermissionsDto(canEdit, canViewMembers);
  }
}
