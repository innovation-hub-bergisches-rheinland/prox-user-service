package de.innovationhub.prox.userservice.domain.organization.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PostOrganizationRequest(@JsonProperty("name") String name) {}
