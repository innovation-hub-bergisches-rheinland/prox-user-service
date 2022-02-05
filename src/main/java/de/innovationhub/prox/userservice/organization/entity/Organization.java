package de.innovationhub.prox.userservice.organization.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
public class Organization {
  @Id
  @Column(name = "id", unique = true, updatable = false, nullable = false)
  @Builder.Default
  private UUID id = UUID.randomUUID();

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "owner_id", updatable = false, nullable = false)
  private UUID owner;

  // TODO: There must be a better approach
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "organization_memberships",
      joinColumns = {
          @JoinColumn(name = "organization_id", referencedColumnName = "id")
      },
      uniqueConstraints = {
          @UniqueConstraint(columnNames = {"user_id", "organization_id"})
      }
  )
  @Builder.Default
  private Set<OrganizationMembership> members = new HashSet<>();
}
