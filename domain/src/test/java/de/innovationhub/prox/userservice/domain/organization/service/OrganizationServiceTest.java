package de.innovationhub.prox.userservice.domain.organization.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.domain.membership.repository.OrganizationMembershipRepository;
import de.innovationhub.prox.userservice.domain.membership.service.OrganizationMembershipService;
import de.innovationhub.prox.userservice.domain.membership.vo.OrganizationRole;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationServiceTest {

  private OrganizationRepository organizationRepository;
  private OrganizationService organizationService;
  private OrganizationMembershipService membershipService;
  private UserService userService;

  @BeforeEach
  void setup() {
    organizationRepository = mock(OrganizationRepository.class);
    userService = mock(UserService.class);
    membershipService = mock(OrganizationMembershipService.class);
    organizationService = new OrganizationService(organizationRepository, userService,
        membershipService);
  }

  @Test
  void should_create_organization_and_return_successfully() {
    // Given
    var principal = "max-mustermann";
    var name = "Musterfirma GmbH & Co. KG";

    // When
    var savedOrg = organizationService.create(name, principal);

    // Then
    assertThat(savedOrg).isNotNull();
    verify(membershipService).create(eq(savedOrg), any(), eq(OrganizationRole.OWNER));
  }
}