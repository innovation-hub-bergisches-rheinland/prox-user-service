package de.innovationhub.prox.userservice.infrastructure.membership.jpa;

import de.innovationhub.prox.userservice.domain.membership.vo.OrganizationRole;
import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organization_memberships",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "organization_id" })
    })
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
public class OrganizationMembershipJpa {

  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @ManyToOne(optional = false, cascade = { CascadeType.ALL })
  @JoinColumn(name = "organization_id", referencedColumnName = "id", updatable = false)
  private OrganizationJpa organization;

  @ManyToOne(optional = false, cascade = { CascadeType.ALL })
  @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
  private UserJpa user;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private OrganizationRole role;
}