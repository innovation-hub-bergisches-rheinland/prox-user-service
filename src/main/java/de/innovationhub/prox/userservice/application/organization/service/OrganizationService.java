package de.innovationhub.prox.userservice.application.organization.service;

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
import de.innovationhub.prox.userservice.domain.user.UserId;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository) {
    this.organizationRepository = organizationRepository;
  }

  public Organization createOrganization(String name, UUID ownerId) {
    // TODO Request Validator
    // TODO: find prox user
    var org = new Organization(new OrganizationId(UUID.randomUUID()), name, new UserId(ownerId));
    organizationRepository.save(org);
    return org;
  }

  public OrganizationMembershipResponse createOrganizationMembership(UUID organizationId, CreateOrganizationMembershipRequest request, UUID authenticatedUserId) {
    var org = organizationRepository.findByIdOptional(new OrganizationId(organizationId)).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().id().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }
    // TODO: verify user ID
    var member =  new UserId(request.userId());
    var membership = new OrganizationMembership(request.role());
    org.addMember(member, membership);
    this.organizationRepository.save(org);

    return new OrganizationMembershipResponse(
        member.id(),
        membership.getRole()
    );
  }

  public OrganizationMembershipResponse updateOrganizationMembership(UUID organizationId, UpdateOrganizationMembershipRequest request, UUID authenticatedUserId) {
    var org = organizationRepository.findByIdOptional(new OrganizationId(organizationId)).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().id().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }
    // TODO: verify user ID
    var member =  new UserId(request.memberId());
    var membership = new OrganizationMembership(request.role());
    org.updateMembership(member, membership);
    this.organizationRepository.save(org);

    return new OrganizationMembershipResponse(
        member.id(),
        membership.getRole()
    );
  }

  public void deleteOrganizationMembership(UUID organizationId, DeleteOrganizationMembershipRequest request, UUID authenticatedUserId) {
    var org = organizationRepository.findByIdOptional(new OrganizationId(organizationId)).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().id().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }

    org.removeMember(new UserId(request.userId()));
    this.organizationRepository.save(org);
  }

  public Optional<Organization> findById(UUID id) {
    return this.organizationRepository.findByIdOptional(new OrganizationId(id));
  }

  public Set<Organization> findAll() {
    return organizationRepository.findAllOrganizations();
  }
}
