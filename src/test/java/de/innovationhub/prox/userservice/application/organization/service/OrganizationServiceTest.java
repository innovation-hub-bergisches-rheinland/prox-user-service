package de.innovationhub.prox.userservice.application.organization.service;

import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {
  @Mock
  OrganizationRepository organizationRepository;

  OrganizationService organizationService;

  @BeforeEach
  void setUp() {

  }
}
