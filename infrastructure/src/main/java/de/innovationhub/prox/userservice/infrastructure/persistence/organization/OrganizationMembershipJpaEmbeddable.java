package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrganizationMembershipJpaEmbeddable {
  @Column(name = "user_id", nullable = false, updatable = false, unique = true)
  private UUID userId;

  @Column(name = "member_role", nullable = false, columnDefinition = "int2")
  private OrganizationRole role;
}
