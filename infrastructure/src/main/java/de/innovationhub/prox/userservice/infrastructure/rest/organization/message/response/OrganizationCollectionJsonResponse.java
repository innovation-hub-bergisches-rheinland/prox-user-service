package de.innovationhub.prox.userservice.infrastructure.rest.organization.message.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;

public record OrganizationCollectionJsonResponse(
    @JsonProperty("organizations") Set<OrganizationJsonResponse> organizations
) { }
