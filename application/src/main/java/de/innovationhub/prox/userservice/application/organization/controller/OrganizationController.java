package de.innovationhub.prox.userservice.application.organization.controller;

import de.innovationhub.prox.userservice.application.organization.message.request.CreateOrganizationRequest;
import de.innovationhub.prox.userservice.application.organization.message.request.FindOrganizationByIdRequest;
import de.innovationhub.prox.userservice.application.organization.message.response.OrganizationResponse;
import java.util.Set;

public interface OrganizationController {
  OrganizationResponse create(CreateOrganizationRequest request);
  OrganizationResponse findById(FindOrganizationByIdRequest request);
  Set<OrganizationResponse> findAll();
}
