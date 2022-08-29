package de.innovationhub.prox.userservice.organization.dto.response;

public record ViewOrganizationProfileDto(
    String foundingDate,
    String numberOfEmployees,
    String homepage,
    String contactEmail,
    String vita,
    String headquarter,
    String quarters,
    ViewSocialMediaDto socialMedia) {}
