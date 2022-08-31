package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.core.data.ObjectNotFoundException;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewAllOrganizationMembershipsDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.entity.OrganizationRole;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationMembershipNotFoundException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.shared.avatar.service.AvatarService;
import de.innovationhub.prox.userservice.user.constraints.IsValidUserId;
import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.repository.UserRepository;
import io.quarkus.security.identity.SecurityIdentity;
import io.smallrye.common.constraint.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class OrganizationService {

  // TODO: Make this configurable
  private static final String AVATAR_KEY_PREFIX = "img/avatars/orgs";

  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final SecurityIdentity securityIdentity;
  private final UserRepository userRepository;
  private final AvatarService avatarService;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMapper organizationMapper,
      SecurityIdentity securityIdentity,
      UserRepository userRepository,
      AvatarService avatarService) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
    this.securityIdentity = securityIdentity;
    this.userRepository = userRepository;
    this.avatarService = avatarService;
  }

  @Transactional
  public ViewOrganizationDto createOrganization(@Valid CreateOrganizationDto request) {
    var userId = UUID.fromString(securityIdentity.getPrincipal().getName());
    Organization org = this.organizationMapper.createFromDto(request);
    var membership = new OrganizationMembership(OrganizationRole.ADMIN);
    org.getMembers().put(userId, membership);
    organizationRepository.save(org);
    return this.organizationMapper.toDto(org);
  }

  @Transactional
  public ViewOrganizationDto updateOrganization(UUID orgId, @Valid CreateOrganizationDto request) {
    var org = findByIdOrThrow(orgId);
    if (!authenticatedUserHasRole(org, OrganizationRole.ADMIN)) {
      throw new ForbiddenOrganizationAccessException();
    }

    organizationMapper.updateOrganization(org, request);
    organizationRepository.save(org);

    return this.organizationMapper.toDto(org);
  }

  public Response getOrganizationAvatar(UUID orgId) throws IOException {
    try {
      var org = findByIdOrThrow(orgId);
      var avatar = org.getAvatar();

      return avatarService.buildAvatarResponse(avatar);
    } catch (ObjectNotFoundException e) {
      throw new WebApplicationException(404);
    }
  }

  @Transactional
  public void setOrganizationAvatar(UUID orgId, FormDataBody formDataBody) throws IOException {
    var org = findByIdOrThrow(orgId);
    if (!authenticatedUserHasRole(org, OrganizationRole.ADMIN)) {
      throw new ForbiddenOrganizationAccessException();
    }

    var avatar =
        avatarService.createAvatarFromFormBody(AVATAR_KEY_PREFIX + "/" + orgId, formDataBody);
    org.setAvatar(avatar);

    organizationRepository.save(org);
  }

  @Transactional
  public ViewOrganizationMembershipDto createOrganizationMembership(
      UUID organizationId, @Valid CreateOrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    if (!authenticatedUserHasRole(org, OrganizationRole.ADMIN)) {
      throw new ForbiddenOrganizationAccessException();
    }

    var memberToAdd = request.member();
    var role = request.role();

    var membership = new OrganizationMembership(role);
    org.getMembers().put(memberToAdd, membership);
    this.organizationRepository.save(org);

    return new ViewOrganizationMembershipDto(
        memberToAdd, resolveUserName(memberToAdd), membership.getRole());
  }

  @Transactional
  public ViewOrganizationMembershipDto updateOrganizationMembership(
      UUID organizationId, UUID memberId, @Valid UpdateOrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    if (!authenticatedUserHasRole(org, OrganizationRole.ADMIN)) {
      throw new ForbiddenOrganizationAccessException();
    }

    var role = request.role();

    var membership = org.getMembers().get(memberId);
    if (membership == null) {
      throw new OrganizationMembershipNotFoundException();
    }

    if (membership.getRole() == OrganizationRole.ADMIN
        && request.role() != OrganizationRole.ADMIN) {
      boolean hasMoreAdmins =
          org.getMembers().entrySet().stream()
              .filter(m -> !m.getKey().equals(memberId))
              .anyMatch(m -> m.getValue().getRole() == OrganizationRole.ADMIN);
      if (!hasMoreAdmins) {
        throw new WebApplicationException("Can't remove the last admin", 422);
      }
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
    if (!authenticatedUserHasRole(org, OrganizationRole.ADMIN)) {
      throw new ForbiddenOrganizationAccessException();
    }

    var membership = org.getMembers().get(memberId);
    if (membership == null) {
      throw new OrganizationMembershipNotFoundException();
    }

    if (membership.getRole() == OrganizationRole.ADMIN) {
      boolean hasMoreAdmins =
          org.getMembers().entrySet().stream()
              .filter(m -> !m.getKey().equals(memberId))
              .anyMatch(m -> m.getValue().getRole() == OrganizationRole.ADMIN);
      if (!hasMoreAdmins) {
        throw new WebApplicationException("Can't remove the last admin", 422);
      }
    }

    org.getMembers().remove(memberId);
    this.organizationRepository.save(org);
  }

  public ViewAllOrganizationMembershipsDto getOrganizationMemberships(UUID organizationId) {
    var org = findByIdOrThrow(organizationId);
    if (!authenticatedUserIsMember(org)) {
      throw new ForbiddenOrganizationAccessException();
    }

    var members =
        org.getMembers().entrySet().stream()
            .map(
                entry ->
                    organizationMapper.toDto(
                        entry.getKey(), resolveUserName(entry.getKey()), entry.getValue()))
            .toList();

    return new ViewAllOrganizationMembershipsDto(members);
  }

  public Optional<ViewOrganizationDto> findById(UUID id) {
    return this.organizationRepository.findById(id).map(this.organizationMapper::toDto);
  }

  public List<ViewOrganizationDto> findAll() {
    return organizationRepository.findAll().stream().map(this.organizationMapper::toDto).toList();
  }

  public List<ViewOrganizationDto> findOrganizationsWhereUserIsMember(@IsValidUserId UUID userId) {
    return this.organizationRepository.findAllWithUserAsMember(userId).stream()
        .map(this.organizationMapper::toDto)
        .toList();
  }

  /**
   * Reconciles a Organization, which means that its state will be re-distributed
   *
   * @param organizationId ID of the organization to reconcile
   */
  @Transactional
  public void reconcile(UUID organizationId) {
    var org = findByIdOrThrow(organizationId);
    // Just saving is enough
    this.organizationRepository.save(org);
  }

  private Organization findByIdOrThrow(UUID id) {
    return organizationRepository.findById(id).orElseThrow(OrganizationNotFoundException::new);
  }

  private String resolveUserName(UUID id) {
    return this.userRepository.findById(id).map(User::getName).orElse(id.toString());
  }

  private boolean authenticatedUserIsMember(Organization org) {
    return getMembershipOfAuthenticatedUser(org) != null;
  }

  private boolean authenticatedUserHasRole(Organization org, OrganizationRole role) {
    var membership = getMembershipOfAuthenticatedUser(org).getRole();
    return membership != null && membership == role;
  }

  private @Nullable OrganizationMembership getMembershipOfAuthenticatedUser(Organization org) {
    return org.getMembers().get(UUID.fromString(securityIdentity.getPrincipal().getName()));
  }
}
