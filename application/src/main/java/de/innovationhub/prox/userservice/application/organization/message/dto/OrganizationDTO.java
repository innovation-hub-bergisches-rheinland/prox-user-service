package de.innovationhub.prox.userservice.application.organization.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public record OrganizationDTO(@JsonProperty("id") UUID id, @JsonProperty("name") String name) {

}
