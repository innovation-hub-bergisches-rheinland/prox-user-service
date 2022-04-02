package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.core.data.ObjectNotFoundException;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.shared.avatar.service.AvatarService;
import de.innovationhub.prox.userservice.user.dto.UserProfileRequestDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.repository.UserProfileRepository;
import de.innovationhub.prox.userservice.user.repository.UserRepository;
import io.quarkus.security.identity.SecurityIdentity;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class UserServiceImpl implements UserService {
  private static final String AVATAR_KEY_PREFIX = "img/avatars/users";

  private final OrganizationService organizationService;
  private final SecurityIdentity securityIdentity;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserProfileRepository userProfileRepository;
  private final AvatarService avatarService;

  @Inject
  public UserServiceImpl(
      OrganizationService organizationService,
      SecurityIdentity securityIdentity,
      UserRepository userRepository,
      UserMapper userMapper,
      UserProfileRepository userProfileRepository,
      AvatarService avatarService) {
    this.userRepository = userRepository;
    this.organizationService = organizationService;
    this.securityIdentity = securityIdentity;
    this.userMapper = userMapper;
    this.userProfileRepository = userProfileRepository;
    this.avatarService = avatarService;
  }

  @Override
  public Optional<UserSearchResponseDto> findById(UUID id) {
    return this.userRepository.findById(id).map(userMapper::toDto);
  }

  @Override
  public boolean existsById(UUID id) {
    return this.userRepository.existsById(id);
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
    return userProfileRepository.findProfileByUserId(id).map(userMapper::toDto);
  }

  @Transactional
  @Override
  public UserProfileResponseDto saveUserProfile(UUID id, UserProfileRequestDto requestDto) {
    if (!securityIdentity.getPrincipal().getName().equals(id.toString()))
      throw new WebApplicationException(403);
    if (!this.userRepository.existsById(id)) throw new WebApplicationException(404);

    var entity = userMapper.toEntity(id, requestDto);
    this.userProfileRepository.save(entity);

    return findProfileByUserId(id).orElseThrow(() -> new WebApplicationException(500));
  }

  public Response getAvatar(UUID userId) throws IOException {
    try {
      var userProfile =
          this.userProfileRepository
              .findProfileByUserId(userId)
              .orElseThrow(() -> new WebApplicationException(404));
      var avatar = userProfile.getAvatar();

      return avatarService.buildAvatarResponse(avatar);
    } catch (ObjectNotFoundException e) {
      throw new WebApplicationException(404);
    }
  }

  @Transactional
  public void setAvatar(UUID userId, FormDataBody formDataBody) throws IOException {
    if (!securityIdentity.getPrincipal().getName().equals(userId.toString()))
      throw new WebApplicationException(403);
    if (!this.userRepository.existsById(userId)) throw new WebApplicationException(404);

    var userProfile =
        this.userProfileRepository
            .findProfileByUserId(userId)
            .orElseThrow(() -> new WebApplicationException(404));

    var avatar =
        avatarService.createAvatarFromFormBody(AVATAR_KEY_PREFIX + "/" + userId, formDataBody);
    userProfile.setAvatar(avatar);
    userProfileRepository.save(userProfile);
  }
}
