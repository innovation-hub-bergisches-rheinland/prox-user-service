package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.core.data.FormDataBody;
import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileBriefCollectionResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileRequestDto;
import de.innovationhub.prox.userservice.user.dto.UserProfileResponseDto;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.core.Response;

public interface UserService {
  Optional<UserSearchResponseDto> findById(UUID id);

  UserProfileBriefCollectionResponseDto findAll();

  boolean existsById(UUID id);

  /**
   * Reconciles a User, which means that its state will be re-distributed
   *
   * @param id ID of the user to reconcile
   */
  void reconcile(UUID id);

  Iterable<UserSearchResponseDto> search(String query);

  List<ViewOrganizationDto> findOrganizationsOfAuthenticatedUser();

  Optional<UserProfileResponseDto> findProfileByUserId(UUID id);

  UserProfileResponseDto saveUserProfile(UUID id, UserProfileRequestDto requestDto);

  void setAvatar(UUID userId, FormDataBody formDataBody) throws IOException;

  Response getAvatar(UUID userId) throws IOException;
}
