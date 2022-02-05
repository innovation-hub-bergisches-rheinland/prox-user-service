package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.request.DeleteOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipRequest;
import de.innovationhub.prox.userservice.organization.dto.response.OrganizationMembershipResponse;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

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
    Organization org = Organization.builder()
            .name(name)
            .owner(ownerId)
            .build();
    organizationRepository.save(org);
    return org;
  }

  @Transactional
  public OrganizationMembershipResponse createOrganizationMembership(UUID organizationId, CreateOrganizationMembershipRequest request, UUID authenticatedUserId) {
    var all = organizationRepository.findAll();
    var org = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }
    // TODO: verify user ID
    var member =  request.userId();
    var membership = new OrganizationMembership(request.role());
    org.getMembers().put(member, membership);
    this.organizationRepository.save(org);

    return new OrganizationMembershipResponse(
        member,
        membership.getRole()
    );
  }

  @Transactional
  public OrganizationMembershipResponse updateOrganizationMembership(UUID organizationId, UpdateOrganizationMembershipRequest request, UUID authenticatedUserId) {
    var org = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }
    // TODO: verify user ID
    var member =  request.memberId();
    var membership = new OrganizationMembership(request.role());
    org.getMembers().put(member, membership);
    this.organizationRepository.save(org);

    return new OrganizationMembershipResponse(
        member,
        membership.getRole()
    );
  }

  @Transactional
  public void deleteOrganizationMembership(UUID organizationId, DeleteOrganizationMembershipRequest request, UUID authenticatedUserId) {
    var org = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }

    org.getMembers().remove(request.userId());
    this.organizationRepository.save(org);
  }

  public Optional<Organization> findById(UUID id) {
    return this.organizationRepository.findById(id);
  }

  public List<Organization> findAll() {
    return organizationRepository.findAll();
  }
}
