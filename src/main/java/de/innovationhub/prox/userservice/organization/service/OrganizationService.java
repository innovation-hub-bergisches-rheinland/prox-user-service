package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.organization.dto.OrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final SecurityIdentity securityIdentity;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMapper organizationMapper,
      SecurityIdentity securityIdentity) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
    this.securityIdentity = securityIdentity;
  }

  public OrganizationDto createOrganization(@Valid OrganizationDto request) {
    var userId = UUID.fromString(securityIdentity.getPrincipal().getName());
    Organization org = Organization.builder()
            .name(request.name())
            .owner(userId)
            .build();
    securityIdentity.getPrincipal().getName();
    organizationRepository.save(org);
    return this.organizationMapper.toDto(org);
  }

  @Transactional
  public OrganizationMembershipDto setOrganizationMembership(UUID organizationId, @Valid OrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    if(!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
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
  public void deleteOrganizationMembership(UUID organizationId, UUID memberId) {
    var org = findByIdOrThrow(organizationId);
    if(!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
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

  private Organization findByIdOrThrow(UUID id) {
    return organizationRepository.findById(id).orElseThrow(OrganizationNotFoundException::new);
  }
}
