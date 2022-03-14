package de.innovationhub.prox.userservice.organization.entity.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import java.util.Set;
import java.util.UUID;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "organization_profiles")
public class OrganizationProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "organization_id")
  @JsonIgnore
  private UUID id = UUID.randomUUID();

  @MapsId
  @OneToOne(optional = false)
  @JoinColumn(name = "organization_id")
  @JsonIgnore
  private Organization organization;

  @Column(name = "founding_date", length = 255)
  @Size(max = 255)
  private String foundingDate;

  // String because inputs like '100-200', 'about 200' or '10 (DE), 20 (International)' are possible
  @Column(name = "number_of_employees", length = 255)
  @Size(max = 255)
  private String numberOfEmployees;

  @Column(name = "homepage", length = 255)
  @Size(max = 255)
  private String homepage;

  @Column(name = "contact_email", length = 255)
  @Size(max = 255)
  @Email
  private String contactEmail;

  @Column(name = "vita", columnDefinition = "TEXT")
  @Lob
  private String vita;

  @Embedded
  @Valid
  @AttributeOverrides({
    @AttributeOverride(name = "location", column = @Column(name = "headquarter_location"))
  })
  private Quarter headquarter;

  @ElementCollection
  @JsonInclude(Include.ALWAYS)
  @CollectionTable(
      name = "organization_profile_quarters",
      joinColumns = @JoinColumn(name = "organization_profile_id"))
  private Set<Quarter> quarters;

  @ElementCollection
  @JsonInclude(Include.ALWAYS)
  @CollectionTable(
      name = "organization_profile_branches",
      joinColumns = @JoinColumn(name = "organization_profile_id"))
  private Set<Branch> branches;

  @Embedded @Valid private SocialMedia socialMedia;

  public OrganizationProfile(
      UUID id,
      Organization organization,
      String foundingDate,
      String numberOfEmployees,
      String homepage,
      String contactEmail,
      String vita,
      Quarter headquarter,
      Set<Quarter> quarters,
      Set<Branch> branches,
      SocialMedia socialMedia) {
    this.id = id;
    this.organization = organization;
    this.foundingDate = foundingDate;
    this.numberOfEmployees = numberOfEmployees;
    this.homepage = homepage;
    this.contactEmail = contactEmail;
    this.vita = vita;
    this.headquarter = headquarter;
    this.quarters = quarters;
    this.branches = branches;
    this.socialMedia = socialMedia;
  }
}
