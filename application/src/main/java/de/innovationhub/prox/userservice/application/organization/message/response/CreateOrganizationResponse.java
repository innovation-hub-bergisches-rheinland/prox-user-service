package de.innovationhub.prox.userservice.application.organization.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.innovationhub.prox.userservice.application.organization.message.dto.OrganizationDTO;
import java.util.UUID;

public record CreateOrganizationResponse(@JsonProperty("organization") OrganizationDTO organization) {

}
