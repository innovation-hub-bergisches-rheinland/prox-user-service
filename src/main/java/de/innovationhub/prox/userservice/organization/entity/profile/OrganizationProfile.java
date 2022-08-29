package de.innovationhub.prox.userservice.organization.entity.profile;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Lob;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrganizationProfile {
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

  /*
   * EMail validation is disabled because it is a valid use case to specify multiple EMail addresses
   * which we currently do not offer. Since the Email itself does not have any clear
   * semantics at the moment we simply allow any kind of string. For the future it is thinkable to
   * deprecate the organization-wide email address and simple list members with their EMail address
   * as representatives
   */
  @Column(name = "contact_email", length = 255)
  @Size(max = 255)
  // @Email
  private String contactEmail;

  @Column(name = "vita", columnDefinition = "TEXT")
  @Lob
  private String vita;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "location", column = @Column(name = "headquarter_location"))
  })
  private Quarter headquarter;

  @Embedded
  @AttributeOverrides({
    @AttributeOverride(name = "location", column = @Column(name = "quarter_location"))
  })
  private Quarter quarters;

  @Embedded @Valid private SocialMedia socialMedia;

  public OrganizationProfile(
      String foundingDate,
      String numberOfEmployees,
      String homepage,
      String contactEmail,
      String vita,
      Quarter headquarter,
      Quarter quarters,
      SocialMedia socialMedia) {
    this.foundingDate = foundingDate;
    this.numberOfEmployees = numberOfEmployees;
    this.homepage = homepage;
    this.contactEmail = contactEmail;
    this.vita = vita;
    this.headquarter = headquarter;
    this.quarters = quarters;
    this.socialMedia = socialMedia;
  }
}
