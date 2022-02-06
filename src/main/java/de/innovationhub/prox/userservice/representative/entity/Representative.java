package de.innovationhub.prox.userservice.representative.entity;

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
public class Representative {
  @Id
  @Column(name = "id", unique = true, updatable = false, nullable = false)
  private UUID id;

  @Column(name = "owner_id", unique = true, updatable = false, nullable = false)
  private UUID owner;

  @Column(name = "name", nullable = false)
  private String name;
}
