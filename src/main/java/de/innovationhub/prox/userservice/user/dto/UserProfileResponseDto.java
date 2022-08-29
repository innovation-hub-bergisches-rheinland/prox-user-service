package de.innovationhub.prox.userservice.user.dto;

import java.util.Set;

public record UserProfileResponseDto(
    String name,
    String affiliation,
    String mainSubject,
    ContactInformationResponseDto contactInformation,
    Set<String> publications,
    String vita) {
  public record ContactInformationResponseDto(
      String room,
      String consultationHour,
      String email,
      String telephone,
      String homepage,
      String collegePage) {}
}
