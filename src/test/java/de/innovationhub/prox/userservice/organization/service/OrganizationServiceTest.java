package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// TODO: At the moment we cover everything using some (poorly written) integration tests.
@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {
  @Mock OrganizationRepository organizationRepository;

  OrganizationService organizationService;

  @BeforeEach
  void setUp() {}
}
