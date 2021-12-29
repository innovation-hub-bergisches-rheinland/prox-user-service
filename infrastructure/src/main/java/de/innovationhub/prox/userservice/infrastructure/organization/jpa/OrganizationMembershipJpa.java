package de.innovationhub.prox.userservice.infrastructure.organization.jpa;

import de.innovationhub.prox.userservice.domain.user.vo.OrganizationRole;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organization_memberships")
@IdClass(OrganizationMembershipId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class OrganizationMembershipJpa {

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private UserJpa user;

  @Id
  @ManyToOne
  @JoinColumn(name = "organization_id", referencedColumnName = "id")
  private OrganizationJpa organization;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private OrganizationRole role;
}
