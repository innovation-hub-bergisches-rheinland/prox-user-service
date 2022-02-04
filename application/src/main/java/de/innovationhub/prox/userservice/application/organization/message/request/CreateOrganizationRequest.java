package de.innovationhub.prox.userservice.application.organization.message.request;

import java.util.UUID;

public record CreateOrganizationRequest(String name, UUID ownerId) {

}
