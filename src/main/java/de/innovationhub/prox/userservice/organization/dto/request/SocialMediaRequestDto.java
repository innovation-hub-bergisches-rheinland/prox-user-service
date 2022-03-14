package de.innovationhub.prox.userservice.organization.dto.request;

public record SocialMediaRequestDto(
    String facebookHandle,
    String twitterHandle,
    String instagramHandle,
    String xingHandle,
    String linkedInHandle) {}
