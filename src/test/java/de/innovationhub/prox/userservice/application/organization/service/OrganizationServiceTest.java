package de.innovationhub.prox.userservice.application.organization.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.request.DeleteOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.response.OrganizationMembershipResponse;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {
  @Mock
  OrganizationRepository organizationRepository;

  OrganizationService organizationService;

  @BeforeEach
  void setUp() {
    this.organizationService = new OrganizationService(organizationRepository);
  }
}
