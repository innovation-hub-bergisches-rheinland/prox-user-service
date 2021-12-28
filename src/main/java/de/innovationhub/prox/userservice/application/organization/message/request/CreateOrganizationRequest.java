package de.innovationhub.prox.userservice.application.organization.message.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateOrganizationRequest(@JsonProperty("name") String name) {}
