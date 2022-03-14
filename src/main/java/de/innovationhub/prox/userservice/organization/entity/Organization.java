package de.innovationhub.prox.userservice.organization.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.innovationhub.prox.userservice.organization.entity.profile.OrganizationProfile;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class Organization {
  @Id
  @Column(name = "id", unique = true, updatable = false, nullable = false)
  @Builder.Default
  private UUID id = UUID.randomUUID();

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "owner_id", updatable = false, nullable = false)
  private UUID owner;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
      name = "organization_memberships",
      joinColumns = {@JoinColumn(name = "organization_id", referencedColumnName = "id")},
      uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "organization_id"})})
  @MapKeyColumn(name = "user_id")
  @Builder.Default
  private Map<UUID, OrganizationMembership> members = new HashMap<>();

  @Embedded private OrganizationProfile profile;

  @Embedded @JsonIgnore private OrganizationAvatar avatar;
}
