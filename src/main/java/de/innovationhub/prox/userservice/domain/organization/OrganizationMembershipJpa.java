package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.user.UserJpa;
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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organization_memberships")
@IdClass(OrganizationMembershipId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
