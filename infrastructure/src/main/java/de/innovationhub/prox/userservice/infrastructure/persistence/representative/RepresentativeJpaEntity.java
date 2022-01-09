package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "representative")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RepresentativeJpaEntity {
  @Id
  @Column(name = "id", unique = true, updatable = false, nullable = false)
  private UUID id;

  @Column(name = "owner_principal", unique = true, updatable = false, nullable = false)
  private String owner;

  @Column(name = "name", nullable = false)
  private String name;
}