package de.innovationhub.prox.userservice.organization.entity;

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
public class OrganizationMembership {
  @Column(name = "member_role", nullable = false, columnDefinition = "int2")
  private OrganizationRole role;
}
