package de.innovationhub.prox.userservice.organization.service;

import de.innovationhub.prox.userservice.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.request.CreateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.request.UpdateOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewAllOrganizationMembershipsDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationMembershipDto;
import de.innovationhub.prox.userservice.organization.entity.Organization;
import de.innovationhub.prox.userservice.organization.entity.OrganizationMembership;
import de.innovationhub.prox.userservice.organization.exception.ForbiddenOrganizationAccessException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationMembershipNotFoundException;
import de.innovationhub.prox.userservice.organization.exception.OrganizationNotFoundException;
import de.innovationhub.prox.userservice.organization.repository.OrganizationRepository;
import de.innovationhub.prox.userservice.user.dto.UserResponseDto;
import de.innovationhub.prox.userservice.user.service.UserService;
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
  private final UserService userService;

  @Inject
  public OrganizationService(
      OrganizationRepository organizationRepository,
      OrganizationMapper organizationMapper,
      SecurityIdentity securityIdentity,
      UserService userService) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
    this.securityIdentity = securityIdentity;
    this.userService = userService;
  }

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
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }
    var member = request.member();
    var membership = new OrganizationMembership(request.role());
    org.getMembers().put(member, membership);
    this.organizationRepository.save(org);

    return new ViewOrganizationMembershipDto(member, resolveUserName(member), membership.getRole());
  }

  @Transactional
  public ViewOrganizationMembershipDto updateOrganizationMembership(
      UUID organizationId, UUID memberId, @Valid UpdateOrganizationMembershipDto request) {
    var org = findByIdOrThrow(organizationId);
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }

    var membership = org.getMembers().get(memberId);
    if (membership == null) {
      throw new OrganizationMembershipNotFoundException();
    }

    membership.setRole(request.role());
    org.getMembers().put(memberId, membership);
    this.organizationRepository.save(org);

    return new ViewOrganizationMembershipDto(
        memberId, resolveUserName(memberId), membership.getRole());
  }

  @Transactional
  public void deleteOrganizationMembership(UUID organizationId, UUID memberId) {
    var org = findByIdOrThrow(organizationId);
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
      throw new ForbiddenOrganizationAccessException();
    }

    org.getMembers().remove(memberId);
    this.organizationRepository.save(org);
  }

  public ViewAllOrganizationMembershipsDto getOrganizationMemberships(UUID organizationId) {
    var org = findByIdOrThrow(organizationId);
    if (!org.getOwner().toString().equals(securityIdentity.getPrincipal().getName())) {
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

  private Organization findByIdOrThrow(UUID id) {
    return organizationRepository.findById(id).orElseThrow(OrganizationNotFoundException::new);
  }

  private String resolveUserName(UUID id) {
    return this.userService.findById(id).map(UserResponseDto::getName).orElse(id.toString());
  }
}
