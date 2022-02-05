package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.organization.dto.OrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMapper organizationMapper) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
  }

  public OrganizationDto createOrganization(String name, UUID ownerId) {
    // TODO Request Validator
    // TODO: find prox user
    Organization org = Organization.builder()
            .name(name)
            .owner(ownerId)
            .build();
    organizationRepository.save(org);
    return this.organizationMapper.toDto(org);
  }

  @Transactional
  public OrganizationMembershipDto createOrganizationMembership(UUID organizationId, OrganizationMembershipDto request, UUID authenticatedUserId) {
    var all = organizationRepository.findAll();
    var org = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }
    // TODO: verify user ID
    var member =  request.member();
    var membership = new OrganizationMembership(request.role());
    org.getMembers().put(member, membership);
    this.organizationRepository.save(org);

    return new OrganizationMembershipDto(
        member,
        membership.getRole()
    );
  }

  @Transactional
  public OrganizationMembershipDto updateOrganizationMembership(UUID organizationId, OrganizationMembershipDto request, UUID authenticatedUserId) {
    var org = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }
    // TODO: verify user ID
    var member =  request.member();
    var membership = new OrganizationMembership(request.role());
    org.getMembers().put(member, membership);
    this.organizationRepository.save(org);

    return new OrganizationMembershipDto(
        member,
        membership.getRole()
    );
  }

  @Transactional
  public void deleteOrganizationMembership(UUID organizationId, UUID memberId, UUID authenticatedUserId) {
    var org = organizationRepository.findById(organizationId).orElseThrow(() -> new OrganizationNotFoundException());
    if(!org.getOwner().equals(authenticatedUserId)) {
      throw new ForbiddenOrganizationAccessException();
    }

    org.getMembers().remove(memberId);
    this.organizationRepository.save(org);
  }

  public Optional<OrganizationDto> findById(UUID id) {
    return this.organizationRepository.findById(id)
        .map(this.organizationMapper::toDto);
  }

  public List<OrganizationDto> findAll() {
    return organizationRepository.findAll()
        .stream().map(this.organizationMapper::toDto)
        .collect(Collectors.toList());
  }
}
