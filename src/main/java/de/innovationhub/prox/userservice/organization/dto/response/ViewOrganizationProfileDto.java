package de.innovationhub.prox.userservice.organization.dto.response;

import java.util.Set;

public record ViewOrganizationProfileDto(
    String foundingDate,
    String numberOfEmployees,
    String homepage,
    String contactEmail,
    String vita,
    String headquarter,
    String quarters,
    Set<String> branches,
    ViewSocialMediaDto socialMedia) {}
