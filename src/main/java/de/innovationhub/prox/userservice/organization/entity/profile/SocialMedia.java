package de.innovationhub.prox.userservice.organization.entity.profile;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class SocialMedia {
  @Size(max = 255)
  @Pattern(regexp = "^[^/.]*$")
  @Column(name = "facebook_handle", length = 255)
  private String facebookHandle;

  @Size(max = 255)
  @Pattern(regexp = "^[^/.]*$")
  @Column(name = "twitter_handle", length = 255)
  private String twitterHandle;

  @Size(max = 255)
  @Pattern(regexp = "^[^/.]*$")
  @Column(name = "instagram_handle", length = 255)
  private String instagramHandle;

  @Size(max = 255)
  @Pattern(regexp = "^[^/.]*$")
  @Column(name = "xing_handle", length = 255)
  private String xingHandle;

  @Size(max = 255)
  @Pattern(regexp = "^[^/.]*$")
  @Column(name = "linkedin_handle", length = 255)
  private String linkedInHandle;

  @Size(max = 255)
  @Pattern(regexp = "^[^/.]*$")
  @Column(name = "youtube_handle", length = 255)
  private String youtubeHandle;
}
