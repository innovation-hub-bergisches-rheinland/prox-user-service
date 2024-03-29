package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.core.data.ObjectNotFoundException;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.shared.avatar.service.AvatarService;
import de.innovationhub.prox.userservice.user.dto.UserProfileBriefCollectionResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileRequestDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.repository.UserRepository;
import io.quarkus.security.identity.SecurityIdentity;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
public class UserServiceImpl implements UserService {
  private static final String AVATAR_KEY_PREFIX = "img/avatars/users";

  private final OrganizationService organizationService;
  private final SecurityIdentity securityIdentity;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final AvatarService avatarService;

  @Inject
  public UserServiceImpl(
      OrganizationService organizationService,
      SecurityIdentity securityIdentity,
      UserRepository userRepository,
      UserMapper userMapper,
      AvatarService avatarService) {
    this.userRepository = userRepository;
    this.organizationService = organizationService;
    this.securityIdentity = securityIdentity;
    this.userMapper = userMapper;
    this.avatarService = avatarService;
  }

  @Override
  public Optional<UserSearchResponseDto> findById(UUID id) {
    return this.userRepository.findById(id).map(userMapper::toDto);
  }

  @Override
  public UserProfileBriefCollectionResponseDto findAll() {
    return new UserProfileBriefCollectionResponseDto(
        this.userRepository.findAllProfiles().stream().map(userMapper::toBriefDto).toList());
  }

  @Override
  public boolean existsById(UUID id) {
    return this.userRepository.existsById(id);
  }

  @Override
  @Transactional
  public void reconcile(UUID id) {
    var user = this.userRepository.findById(id).orElseThrow(() -> new WebApplicationException(404));
    // Re-save is enough
    this.userRepository.save(user);
  }

  @Override
  public Iterable<UserSearchResponseDto> search(String query) {
    return this.userMapper.toDtoSet(this.userRepository.search(query).stream());
  }

  @Override
  public List<ViewOrganizationDto> findOrganizationsOfAuthenticatedUser() {
    var userId = UUID.fromString(this.securityIdentity.getPrincipal().getName());
    return this.organizationService.findOrganizationsWhereUserIsMember(userId);
  }

  @Override
  public Optional<UserProfileResponseDto> findProfileByUserId(UUID id) {
    var user = this.userRepository.findById(id);
    var profile = user.map(User::getProfile);
    if (profile.isPresent()) return profile.map(userMapper::toDto);
    return userRepository.findById(id).map(userMapper::userToProfile);
  }

  @Transactional
  @Override
  public UserProfileResponseDto saveUserProfile(UUID id, UserProfileRequestDto requestDto) {
    if (!securityIdentity.getPrincipal().getName().equals(id.toString()))
      throw new WebApplicationException(403);

    var user = this.userRepository.findById(id).orElseThrow(() -> new WebApplicationException(404));
    var profile = user.getProfile();
    if (profile != null) {
      userMapper.updateProfile(profile, requestDto);
      user.setProfile(profile);
    } else {
      user.setProfile(userMapper.toEntity(id, requestDto));
    }

    try {
      this.userRepository.save(user);
    } catch (ConstraintViolationException e) {
      log.error("Could not save profile", e);
      throw new WebApplicationException(Status.BAD_REQUEST);
    }

    return findProfileByUserId(id).orElseThrow(() -> new WebApplicationException(500));
  }

  public Response getAvatar(UUID userId) throws IOException {
    try {
      var user = this.userRepository.findById(userId);
      var profile = user.map(User::getProfile).orElseThrow(ObjectNotFoundException::new);
      var avatar = profile.getAvatar();

      return avatarService.buildAvatarResponse(avatar);
    } catch (ObjectNotFoundException e) {
      return Response.status(404).build();
    }
  }

  @Transactional
  public void setAvatar(UUID userId, FormDataBody formDataBody) throws IOException {
    if (!securityIdentity.getPrincipal().getName().equals(userId.toString()))
      throw new WebApplicationException(403);

    var user =
        this.userRepository.findById(userId).orElseThrow(() -> new WebApplicationException(404));
    var profile = user.getProfile();
    if (profile == null) {
      throw new ObjectNotFoundException();
    }

    var avatar =
        avatarService.createAvatarFromFormBody(AVATAR_KEY_PREFIX + "/" + userId, formDataBody);
    profile.setAvatar(avatar);

    user.setProfile(profile);
    userRepository.save(user);
  }
}
