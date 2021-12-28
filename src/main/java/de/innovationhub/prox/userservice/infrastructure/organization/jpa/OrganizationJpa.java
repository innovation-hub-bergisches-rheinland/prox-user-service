package de.innovationhub.prox.userservice.infrastructure.organization.jpa;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an Organization in PROX, examples are companies, laboratories, institutes or other
 * organizations
 */
@Entity
@Getter
@Setter
@Table(name = "organizations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class OrganizationJpa {
  /** Identifier */
  @Id
  @Column(name = "id")
  private UUID id;

  /** Name of the org */
  @Column(name = "name", nullable = false)
  private String name;
}
