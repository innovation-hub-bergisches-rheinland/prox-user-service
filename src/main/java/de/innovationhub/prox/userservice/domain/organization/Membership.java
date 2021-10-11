package de.innovationhub.prox.userservice.domain.organization;

import de.innovationhub.prox.userservice.domain.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "organization_memberships")
@IdClass(MembershipId.class)
public class Membership {

  @Id
  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
  private User user;

  @Id
  @ManyToOne
  @JoinColumn(
    name = "organization_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Organization organization;

  @Column(name = "type", nullable = false)
  private MembershipType type = MembershipType.MEMBER;

  public Membership(User user, Organization organization) {
    this.user = user;
    this.organization = organization;
  }

  protected Membership(
    User user,
    Organization organization,
    MembershipType type
  ) {
    this.user = user;
    this.organization = organization;
    this.type = type;
  }
}
