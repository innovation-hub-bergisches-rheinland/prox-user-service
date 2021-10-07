package de.innovationhub.prox.userservice.domain.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrganizationPostDto(@JsonProperty("name") String name) {}
