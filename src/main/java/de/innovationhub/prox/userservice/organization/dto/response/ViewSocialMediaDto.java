package de.innovationhub.prox.userservice.organization.dto.response;

public record ViewSocialMediaDto(
    String facebookHandle,
    String twitterHandle,
    String instagramHandle,
    String xingHandle,
    String linkedInHandle) {}
