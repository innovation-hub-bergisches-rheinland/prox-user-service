package de.innovationhub.prox.userservice.application.organization.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record ReadOrganizationResponse(@JsonProperty("id") UUID id, @JsonProperty("name") String name) {}
