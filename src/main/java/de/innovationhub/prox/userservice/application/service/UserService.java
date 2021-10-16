package de.innovationhub.prox.userservice.application.service;

import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationMembershipResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.MembershipMapper;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final MembershipMapper membershipMapper;

  @Autowired
  public UserService(
    UserRepository userRepository,
    MembershipMapper membershipMapper
  ) {
    this.userRepository = userRepository;
    this.membershipMapper = membershipMapper;
  }

  @Transactional
  public Flux<GetOrganizationMembershipResponse> findMembershipsOfAuthenticatedUser(
    Authentication authentication
  ) {
    return this.findMembershipsOfUserWithId(
        extractUUIDFromAuthentication(authentication)
      );
  }

  @Transactional
  public Flux<GetOrganizationMembershipResponse> findMembershipsOfUserWithId(
    UUID userId
  ) {
    return Mono
      .fromCallable(() -> userRepository.findMembershipsOfUserWithId(userId))
      .subscribeOn(Schedulers.boundedElastic())
      .flatMapIterable(res -> res)
      .map(membership -> membershipMapper.membershipToOmitUserGetDto(membership)
      );
  }

  private UUID extractUUIDFromAuthentication(Authentication authentication) {
    return UUID.fromString(authentication.getName());
  }
}
