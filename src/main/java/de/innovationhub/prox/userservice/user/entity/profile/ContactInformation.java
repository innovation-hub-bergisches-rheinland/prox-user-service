package de.innovationhub.prox.userservice.user.entity.profile;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ContactInformation {
  @Column(name = "room")
  private String room;

  @Column(name = "consultation_hour")
  private String consultationHour;

  @Column(name = "email")
  private String email;

  @Column(name = "telephone")
  private String telephone;

  @Column(name = "homepage")
  private String homepage;

  @Column(name = "college_page")
  private String collegePage;
}
