package de.innovationhub.prox.userservice.user.dto;

import java.util.Set;

public record UserProfileRequestDto(
    String name,
    String affiliation,
    String mainSubject,
    ContactInformationRequestDto contactInformation,
    Set<String> subjects,
    Set<String> publications,
    String vita) {
  public record ContactInformationRequestDto(
      String room,
      String consultationHour,
      String email,
      String telephone,
      String homepage,
      String collegePage) {}
}
