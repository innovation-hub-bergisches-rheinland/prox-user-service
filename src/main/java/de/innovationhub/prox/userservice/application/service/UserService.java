package de.innovationhub.prox.userservice.application.service;

import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationMembershipResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.MembershipMapper;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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

  public Flux<GetOrganizationMembershipResponse> findMembershipsOfUserWithId(
    UUID userId
  ) {
    return Mono
      .fromCallable(() -> userRepository.findById(userId))
      .subscribeOn(Schedulers.boundedElastic())
      .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
      .flatMapIterable(user -> user.getMembers())
      .map(membership -> membershipMapper.membershipToOmitUserGetDto(membership)
      );
  }
}
