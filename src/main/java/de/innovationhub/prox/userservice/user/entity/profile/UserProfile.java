package de.innovationhub.prox.userservice.user.entity.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.innovationhub.prox.userservice.shared.avatar.entity.Avatar;
import java.util.List;
import java.util.UUID;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserProfile {
  @Id
  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "name")
  private String name;

  private String affiliation;

  @Column(name = "main_subject")
  private String mainSubject;

  @Embedded private ContactInformation contactInformation;

  @ElementCollection
  @CollectionTable(name = "user_research_subjects", joinColumns = @JoinColumn(name = "user_id"))
  private List<ResearchSubject> researchSubjects;

  @ElementCollection
  @CollectionTable(name = "user_publications", joinColumns = @JoinColumn(name = "user_id"))
  private List<Publication> publications;

  @Embedded @JsonIgnore private Avatar avatar;
}
