package de.innovationhub.prox.userservice.application.organization.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import de.innovationhub.prox.userservice.application.organization.dto.request.CreateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.application.organization.dto.request.DeleteOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.application.organization.dto.request.UpdateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.application.organization.dto.response.OrganizationMembershipResponse;
import de.innovationhub.prox.userservice.application.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.application.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.organization.vo.OrganizationRole;
import de.innovationhub.prox.userservice.domain.user.UserId;
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

  @Test
  void shouldThrowNotFoundExceptionWhenAddingMembershipOfNotExistingOrg() {
    // Given
    var organizationId = new OrganizationId(UUID.randomUUID());
    when(this.organizationRepository.findByIdOptional(organizationId)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(OrganizationNotFoundException.class, () -> this.organizationService.createOrganizationMembership(organizationId.id(), null, UUID.randomUUID()));
  }

  @Test
  void shouldReturnResponseWhenAddingMembership() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new CreateOrganizationMembershipRequest(
        UUID.randomUUID(),
        OrganizationRole.MEMBER
    );
    var givenOrg = createDummyOrganization(organizationId, authenticatedUserId);
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When
    var response = this.organizationService.createOrganizationMembership(organizationId.id(), request, authenticatedUserId.id());

    // Then
    assertThat(response)
        .extracting(OrganizationMembershipResponse::userId, OrganizationMembershipResponse::role)
        .containsExactly(request.userId(), request.role());
  }

  @Test
  void shouldSaveMembershipWhenAddingMembership() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new CreateOrganizationMembershipRequest(
        UUID.randomUUID(),
        OrganizationRole.MEMBER
    );
    var givenOrg = createDummyOrganization(organizationId, authenticatedUserId);
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When
    this.organizationService.createOrganizationMembership(organizationId.id(), request, authenticatedUserId.id());

    // Then
    ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
    verify(this.organizationRepository).save(captor.capture());
    Assertions.assertThat(captor.getValue())
        .isNotNull()
        .matches(org -> org.getMembers().get(new UserId(request.userId())).getRole().equals(request.role()));
  }

  @Test
  void shouldThrowForbiddenExceptionWhenAddingMembershipOfOrgWithoutOwnerRole() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var ownerId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new CreateOrganizationMembershipRequest(
        UUID.randomUUID(),
        OrganizationRole.MEMBER
    );
    var givenOrg = createDummyOrganization(organizationId, ownerId);
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When / Then
    assertThrows(ForbiddenOrganizationAccessException.class, () -> this.organizationService.createOrganizationMembership(organizationId.id(), request, authenticatedUserId.id()));
  }

  @Test
  void shouldThrowNotFoundExceptionWhenRemovingMembershipOfNotExistingOrg() {
    // Given
    var organizationId = new OrganizationId(UUID.randomUUID());
    when(this.organizationRepository.findByIdOptional(organizationId)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(OrganizationNotFoundException.class, () -> this.organizationService.deleteOrganizationMembership(organizationId.id(), null, UUID.randomUUID()));
  }

  @Test
  void shouldSaveRemovedMembershipWhenRemovingMembership() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var memberId = UUID.randomUUID();
    var request = new DeleteOrganizationMembershipRequest(
        memberId
    );
    var givenOrg = createDummyOrganization(organizationId, authenticatedUserId);
    givenOrg.addMember(new UserId(memberId), new OrganizationMembership(OrganizationRole.MEMBER));
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When
    this.organizationService.deleteOrganizationMembership(organizationId.id(), request, authenticatedUserId.id());

    // Then
    ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
    verify(this.organizationRepository).save(captor.capture());
    Assertions.assertThat(captor.getValue())
        .isNotNull()
        .matches(org -> org.getMembers().get(new UserId(memberId)) == null);
  }

  @Test
  void shouldThrowForbiddenExceptionWhenRemovingMembershipOfOrgWithoutOwnerRole() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var ownerId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new DeleteOrganizationMembershipRequest(
        UUID.randomUUID()
    );
    var givenOrg = createDummyOrganization(organizationId, ownerId);
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When / Then
    assertThrows(ForbiddenOrganizationAccessException.class, () -> this.organizationService.deleteOrganizationMembership(organizationId.id(), request, authenticatedUserId.id()));
  }

  @Test
  void shouldThrowNotFoundExceptionWhenUpdatingMembershipOfNotExistingOrg() {
    // Given
    var organizationId = new OrganizationId(UUID.randomUUID());
    when(this.organizationRepository.findByIdOptional(organizationId)).thenReturn(Optional.empty());

    // When / Then
    assertThrows(OrganizationNotFoundException.class, () -> this.organizationService.updateOrganizationMembership(organizationId.id(), null, UUID.randomUUID()));
  }

  @Test
  void shouldReturnResponseWhenUpdatingMembership() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new UpdateOrganizationMembershipRequest(
        UUID.randomUUID(),
        OrganizationRole.ADMIN
    );
    var givenOrg = createDummyOrganization(organizationId, authenticatedUserId);
    givenOrg.addMember(new UserId(request.memberId()), new OrganizationMembership(OrganizationRole.MEMBER));
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When
    var response = this.organizationService.updateOrganizationMembership(organizationId.id(), request, authenticatedUserId.id());

    // Then
    assertThat(response)
        .extracting(OrganizationMembershipResponse::userId, OrganizationMembershipResponse::role)
        .containsExactly(request.memberId(), request.role());
  }

  @Test
  void shouldSaveMembershipWhenUpdatingMembership() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new UpdateOrganizationMembershipRequest(
        UUID.randomUUID(),
        OrganizationRole.MEMBER
    );
    var givenOrg = createDummyOrganization(organizationId, authenticatedUserId);
    givenOrg.addMember(new UserId(request.memberId()), new OrganizationMembership(OrganizationRole.MEMBER));
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When
    this.organizationService.updateOrganizationMembership(organizationId.id(), request, authenticatedUserId.id());

    // Then
    ArgumentCaptor<Organization> captor = ArgumentCaptor.forClass(Organization.class);
    verify(this.organizationRepository).save(captor.capture());
    Assertions.assertThat(captor.getValue())
        .isNotNull()
        .matches(org -> org.getMembers().get(new UserId(request.memberId())).getRole().equals(request.role()));
  }

  @Test
  void shouldThrowForbiddenExceptionWhenUpdatingMembershipOfOrgWithoutOwnerRole() {
    // Given
    var authenticatedUserId = new UserId(UUID.randomUUID());
    var ownerId = new UserId(UUID.randomUUID());
    var organizationId = new OrganizationId(UUID.randomUUID());
    var request = new UpdateOrganizationMembershipRequest(
        UUID.randomUUID(),
        OrganizationRole.MEMBER
    );
    var givenOrg = createDummyOrganization(organizationId, ownerId);
    when(this.organizationRepository.findByIdOptional(eq(organizationId))).thenReturn(Optional.of(givenOrg));

    // When / Then
    assertThrows(ForbiddenOrganizationAccessException.class, () -> this.organizationService.updateOrganizationMembership(organizationId.id(), request, authenticatedUserId.id()));
  }

  private Organization createDummyOrganization(OrganizationId organizationId, UserId ownerId) {
    return new Organization(organizationId, "Dummy Organization", ownerId);
  }
}
