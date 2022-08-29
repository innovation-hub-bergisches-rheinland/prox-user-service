package de.innovationhub.prox.userservice.organization.dto.request;

public record OrganizationProfileRequestDto(
    String foundingDate,
    String numberOfEmployees,
    String homepage,
    String contactEmail,
    String vita,
    String headquarter,
    String quarters,
    SocialMediaRequestDto socialMedia) {}
