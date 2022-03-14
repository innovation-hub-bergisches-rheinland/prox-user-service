package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.request.OrganizationProfileRequestDto;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewAllOrganizationMembershipsDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationProfileDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationMembershipNotFoundException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.user.constraints.IsValidUserId;
import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import de.innovationhub.prox.userservice.user.service.UserIdentityService;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;

@ApplicationScoped
public class OrganizationService {
  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final SecurityIdentity securityIdentity;
  private final UserIdentityService userIdentityService;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMapper organizationMapper,
      SecurityIdentity securityIdentity,
      UserIdentityService userIdentityService) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
    this.securityIdentity = securityIdentity;
    this.userIdentityService = userIdentityService;
  }

  @Transactional
  public ViewOrganizationDto createOrganization(@Valid CreateOrganizationDto request) {
    var userId = UUID.fromString(securityIdentity.getPrincipal().getName());
    Organization org = this.organizationMapper.createFromDto(request, userId);
    organizationRepository.save(org);
    return this.organizationMapper.toDto(org);
  }

  @Transactional
  public ViewOrganizationMembershipDto createOrganizationMembership(
      UUID organizationId, @Valid CreateOrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    // TODO: Admins?
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }
    var member = request.member();
    var role = request.role();

    if (role.equals(OrganizationRole.OWNER)) {
      // TODO: explicit exception / validation
      throw new WebApplicationException(422);
    }

    var membership = new OrganizationMembership(role);
    org.getMembers().put(member, membership);
    this.organizationRepository.save(org);

    return new ViewOrganizationMembershipDto(member, resolveUserName(member), membership.getRole());
  }

  @Transactional
  public ViewOrganizationMembershipDto updateOrganizationMembership(
      UUID organizationId, UUID memberId, @Valid UpdateOrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    // TODO: Admins?
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }
    var role = request.role();

    if (role.equals(OrganizationRole.OWNER)) {
      // TODO: explicit exception / validation
      throw new WebApplicationException(422);
    }

    var membership = org.getMembers().get(memberId);
    if (membership == null) {
      throw new OrganizationMembershipNotFoundException();
    }

    membership.setRole(role);
    org.getMembers().put(memberId, membership);
    this.organizationRepository.save(org);

    return new ViewOrganizationMembershipDto(
        memberId, resolveUserName(memberId), membership.getRole());
  }

  @Transactional
  public void deleteOrganizationMembership(UUID organizationId, UUID memberId) {
    var org = findByIdOrThrow(organizationId);
    // TODO: Admins?
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }

    org.getMembers().remove(memberId);
    this.organizationRepository.save(org);
  }

  public ViewAllOrganizationMembershipsDto getOrganizationMemberships(UUID organizationId) {
    var org = findByIdOrThrow(organizationId);
    // TODO: Admins?
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }

    var owner =
        new ViewOrganizationMembershipDto(
            org.getOwner(), resolveUserName(org.getOwner()), OrganizationRole.OWNER);
    var members =
        org.getMembers().entrySet().stream()
            .map(
                entry ->
                    new ViewOrganizationMembershipDto(
                        entry.getKey(),
                        resolveUserName(entry.getKey()),
                        entry.getValue().getRole()))
            .collect(Collectors.toList());
    members.add(owner);

    return new ViewAllOrganizationMembershipsDto(members);
  }

  public Optional<ViewOrganizationDto> findById(UUID id) {
    return this.organizationRepository.findById(id).map(this.organizationMapper::toDto);
  }

  public List<ViewOrganizationDto> findAll() {
    return organizationRepository.findAll().stream()
        .map(this.organizationMapper::toDto)
        .collect(Collectors.toList());
  }

  public List<ViewOrganizationDto> findOrganizationsWhereUserIsMember(@IsValidUserId UUID userId) {
    return this.organizationRepository.findAllWithUserAsMember(userId).stream()
        .map(this.organizationMapper::toDto)
        .collect(Collectors.toList());
  }

  public ViewOrganizationProfileDto findOrganizationProfile(UUID id) {
    return this.organizationMapper.toDto(this.findByIdOrThrow(id).getProfile());
  }

  @Transactional
  public ViewOrganizationProfileDto saveOrganizationProfile(
      UUID orgId, OrganizationProfileRequestDto organizationProfileRequestDto) {
    var org = this.findByIdOrThrow(orgId);

    // TODO: Admins?
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }

    var profile = this.organizationMapper.createFromDto(organizationProfileRequestDto);
    org.setProfile(profile);
    this.organizationRepository.save(org);
    return findOrganizationProfile(orgId);
  }

  private Organization findByIdOrThrow(UUID id) {
    return organizationRepository.findById(id).orElseThrow(OrganizationNotFoundException::new);
  }

  private String resolveUserName(UUID id) {
    return this.userIdentityService
        .findById(id)
        .map(UserResponseDto::getName)
        .orElse(id.toString());
  }
}
