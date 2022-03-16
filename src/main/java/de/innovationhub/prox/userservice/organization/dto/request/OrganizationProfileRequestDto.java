package de.innovationhub.prox.userservice.organization.dto.request;

import java.util.Set;

public record OrganizationProfileRequestDto(
    String foundingDate,
    String numberOfEmployees,
    String homepage,
    String contactEmail,
    String vita,
    String headquarter,
    String quarters,
    Set<String> branches,
    SocialMediaRequestDto socialMedia) {}
