package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.core.data.FileObject;
import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.core.data.ObjectNotFoundException;
import de.innovationhub.prox.userservice.core.data.ObjectStoreRepository;
import de.innovationhub.prox.userservice.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewAllOrganizationMembershipsDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationAvatar;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

@ApplicationScoped
public class OrganizationService {

  private static final String AVATAR_KEY_PREFIX = "img/avatars/orgs";

  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final SecurityIdentity securityIdentity;
  private final UserIdentityService userIdentityService;
  private final ObjectStoreRepository objectStoreRepository;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMapper organizationMapper,
      SecurityIdentity securityIdentity,
      UserIdentityService userIdentityService,
      ObjectStoreRepository objectStoreRepository) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
    this.securityIdentity = securityIdentity;
    this.userIdentityService = userIdentityService;
    this.objectStoreRepository = objectStoreRepository;
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
    var member = org.getMembers().get(securityIdentity.getPrincipal().getName());
    if (member == null || member.getRole() != OrganizationRole.ADMIN) {
      throw new ForbiddenOrganizationAccessException();
    }
    organizationMapper.updateOrganization(org, request);
    organizationRepository.save(org);
    return this.organizationMapper.toDto(org);
  }

  public FileObject getOrganizationAvatar(UUID orgId) throws IOException {
    try {
      var org = findByIdOrThrow(orgId);
      var avatar = org.getAvatar();

      if (avatar == null || avatar.getKey() == null || avatar.getKey().isBlank())
        throw new WebApplicationException(404);

      return objectStoreRepository.getObject(avatar.getKey());
    } catch (ObjectNotFoundException e) {
      throw new WebApplicationException(404);
    }
  }

  @Transactional
  public void setOrganizationAvatar(UUID orgId, FormDataBody formDataBody) throws IOException {
    var org = findByIdOrThrow(orgId);
    var member = org.getMembers().get(UUID.fromString(securityIdentity.getPrincipal().getName()));
    if (member == null || member.getRole() != OrganizationRole.ADMIN) {
      throw new ForbiddenOrganizationAccessException();
    }

    var bytes = formDataBody.getData().readAllBytes();
    var mimeType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(bytes));

    // TODO: We could do some more validation here. Also, we could try to store the image in
    //  different resolutions

    if (!mimeType.equalsIgnoreCase("image/png") && !mimeType.equalsIgnoreCase("image/jpeg"))
      throw new WebApplicationException(Status.BAD_REQUEST);

    String extension = "";

    switch (mimeType.trim().toLowerCase()) {
      case "image/png" -> extension = ".png";
      case "image/jpeg" -> extension = ".jpg";
    }

    var fileObject =
        new FileObject(AVATAR_KEY_PREFIX + "/" + orgId.toString() + extension, mimeType, bytes);

    objectStoreRepository.saveObject(fileObject);
    org.setAvatar(new OrganizationAvatar(fileObject.getKey()));
    organizationRepository.save(org);
  }

  @Transactional
  public ViewOrganizationMembershipDto createOrganizationMembership(
      UUID organizationId, @Valid CreateOrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    var member = org.getMembers().get(UUID.fromString(securityIdentity.getPrincipal().getName()));
    if (member == null || member.getRole() != OrganizationRole.ADMIN) {
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
    var member = org.getMembers().get(UUID.fromString(securityIdentity.getPrincipal().getName()));
    if (member == null || member.getRole() != OrganizationRole.ADMIN) {
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
    var member = org.getMembers().get(UUID.fromString(securityIdentity.getPrincipal().getName()));
    if (member == null || member.getRole() != OrganizationRole.ADMIN) {
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
    var member = org.getMembers().get(UUID.fromString(securityIdentity.getPrincipal().getName()));
    if (member == null) {
      throw new ForbiddenOrganizationAccessException();
    }

    var members =
        org.getMembers().entrySet().stream()
            .map(
                entry ->
                    new ViewOrganizationMembershipDto(
                        entry.getKey(),
                        resolveUserName(entry.getKey()),
                        entry.getValue().getRole()))
            .collect(Collectors.toList());

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
