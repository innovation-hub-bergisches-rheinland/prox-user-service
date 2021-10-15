package de.innovationhub.prox.userservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.domain.organization.MembershipType;
import de.innovationhub.prox.userservice.domain.organization.Organization;
import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.organization.dto.PostOrganizationRequest;
import de.innovationhub.prox.userservice.domain.user.User;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.test.StepVerifier;

@SpringBootTest
class OrganizationServiceTest {

  @MockBean
  OrganizationRepository organizationRepository;

  @Autowired
  OrganizationService organizationService;

  @Test
  void given_emptyUser_when_createOrganization_should_throw() {
    var organizationDto = new PostOrganizationRequest(
      "Musterfirma GmbH & Co. KG"
    );

    assertThrows(
      NullPointerException.class,
      () -> organizationService.createOrganization(organizationDto, null)
    );
  }

  @Test
  void given_user_when_createOrganization_should_create() {
    var organizationDto = new PostOrganizationRequest(
      "Musterfirma GmbH & Co. KG"
    );
    var user = new User(UUID.randomUUID());
    when(organizationRepository.save(any()))
      .thenAnswer(invocation -> invocation.getArgument(0));

    StepVerifier
      .create(organizationService.createOrganization(organizationDto, user))
      .assertNext(next -> {
        assertThat(next.name()).isEqualTo("Musterfirma GmbH & Co. KG");
        assertThat(next.id()).isNotNull();
      })
      .expectComplete()
      .verify();

    verify(organizationRepository).save(any());
  }

  @Test
  void given_organization_when_getOrganization_should_find() {
    var user = new User(UUID.randomUUID());
    var organization = new Organization("Musterfirma GmbH & Co. KG", user);
    when(organizationRepository.findById(eq(organization.getId())))
      .thenReturn(Optional.of(organization));

    StepVerifier
      .create(organizationService.getOrganizationWithId(organization.getId()))
      .assertNext(next -> {
        assertThat(next.name()).isEqualTo("Musterfirma GmbH & Co. KG");
        assertThat(next.id()).isEqualTo(organization.getId());
      })
      .expectComplete()
      .verify();

    verify(organizationRepository).findById(eq(organization.getId()));
  }

  @Test
  void given_noOrganization_when_getOrganization_should_empty() {
    var orgId = UUID.randomUUID();
    when(organizationRepository.findById(eq(orgId)))
      .thenReturn(Optional.empty());

    StepVerifier
      .create(organizationService.getOrganizationWithId(orgId))
      .expectComplete()
      .verify();

    verify(organizationRepository).findById(eq(orgId));
  }

  @Test
  void given_organization_when_findMemberships_should_find() {
    var owner = new User(UUID.randomUUID());
    var organization = new Organization("Musterfirma GmbH & Co. KG", owner);
    User member = new User(UUID.randomUUID());
    organization.addMember(member);
    when(organizationRepository.findById(eq(organization.getId())))
      .thenReturn(Optional.of(organization));

    StepVerifier
      .create(
        organizationService.findOrganizationMemberships(organization.getId())
      )
      .recordWith(HashSet::new)
      .thenConsumeWhile(x -> true)
      .expectRecordedMatches(memberships -> {
        assertThat(memberships)
          .extracting("user.id", "type")
          .containsExactlyInAnyOrder(
            tuple(owner.getId(), MembershipType.OWNER),
            tuple(member.getId(), MembershipType.MEMBER)
          );
        return true;
      })
      .expectComplete()
      .verify();

    verify(organizationRepository).findById(eq(organization.getId()));
  }

  @Test
  void given_noOrganization_when_findMemberships_should_empty() {
    var orgId = UUID.randomUUID();
    when(organizationRepository.findById(eq(orgId)))
      .thenReturn(Optional.empty());

    StepVerifier
      .create(organizationService.findOrganizationMemberships(orgId))
      .expectComplete()
      .verify();

    verify(organizationRepository).findById(eq(orgId));
  }
}
