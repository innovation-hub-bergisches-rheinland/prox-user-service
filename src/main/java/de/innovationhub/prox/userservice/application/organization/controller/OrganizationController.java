package de.innovationhub.prox.userservice.application.organization.controller;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.CreateOrganizationResponse;

public interface OrganizationController {
  CreateOrganizationResponse createOrganization(CreateOrganizationRequest createOrganizationRequest);
}
