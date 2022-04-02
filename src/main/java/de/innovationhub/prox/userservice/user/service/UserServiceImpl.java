package de.innovationhub.prox.userservice.user.service;

import de.innovationhub.prox.userservice.organization.dto.response.ViewOrganizationDto;
import de.innovationhub.prox.userservice.organization.service.OrganizationService;
import de.innovationhub.prox.userservice.user.dto.UserSearchResponseDto;
import de.innovationhub.prox.userservice.user.entity.UserMapper;
import de.innovationhub.prox.userservice.user.repository.UserRepository;
import io.quarkus.security.identity.SecurityIdentity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class UserServiceImpl implements UserService {
  private final OrganizationService organizationService;
  private final SecurityIdentity securityIdentity;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Inject
  public UserServiceImpl(
      OrganizationService organizationService,
      SecurityIdentity securityIdentity,
      UserRepository userRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.organizationService = organizationService;
    this.securityIdentity = securityIdentity;
    this.userMapper = userMapper;
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
}
