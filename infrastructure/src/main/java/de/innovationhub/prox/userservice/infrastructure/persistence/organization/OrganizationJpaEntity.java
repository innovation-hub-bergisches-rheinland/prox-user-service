package de.innovationhub.prox.userservice.infrastructure.persistence.organization;

import java.util.Set;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class OrganizationJpaEntity {
  @Id
  @Column(name = "id", unique = true, updatable = false, nullable = false)
  private UUID id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "owner_principal", updatable = false, nullable = false)
  private String owner;

  // TODO: There must be a better approach
  @ElementCollection
  @CollectionTable(
      name = "organization_memberships",
      joinColumns = {
          @JoinColumn(name = "organization_id", referencedColumnName = "id")
      },
      uniqueConstraints = {
          @UniqueConstraint(columnNames = {"user_principal", "organization_id"})
      }
  )
  private Set<OrganizationMembershipJpaEmbeddable> members;
}
