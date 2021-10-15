package de.innovationhub.prox.userservice.application.service;

import de.innovationhub.prox.userservice.domain.organization.Organization;
import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.GetUserMembershipResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.MembershipMapper;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.domain.organization.dto.PostOrganizationRequest;
import de.innovationhub.prox.userservice.domain.organization.dto.PostOrganizationResponse;
import de.innovationhub.prox.userservice.domain.user.User;
import java.util.Objects;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;
  private final MembershipMapper membershipMapper;

  public OrganizationService(
    OrganizationRepository organizationRepository,
    OrganizationMapper organizationMapper,
    MembershipMapper membershipMapper
  ) {
    this.organizationRepository = organizationRepository;
    this.organizationMapper = organizationMapper;
    this.membershipMapper = membershipMapper;
  }

  private Mono<Organization> getOrganizationEntityWithId(UUID id) {
    return Mono
      .fromCallable(() -> organizationRepository.findById(id))
      .subscribeOn(Schedulers.boundedElastic())
      .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty));
  }

  public Mono<GetOrganizationResponse> getOrganizationWithId(UUID id) {
    return this.getOrganizationEntityWithId(id)
      .map(this.organizationMapper::organizationToGetDto);
  }

  public Flux<GetUserMembershipResponse> findOrganizationMemberships(UUID id) {
    return this.getOrganizationEntityWithId(id)
      .flatMapIterable(org -> org.getMembers())
      .map(member ->
        this.membershipMapper.membershipToOmitOrganizationGetDto(member)
      );
  }

  @Transactional(TxType.REQUIRED)
  public Mono<PostOrganizationResponse> createOrganization(
    PostOrganizationRequest org,
    User owner
  ) {
    Objects.requireNonNull(org);
    Objects.requireNonNull(owner);
    return Mono
      .fromCallable(() ->
        organizationRepository.save(mapOrgPostDtoToEntity(org, owner))
      )
      .subscribeOn(Schedulers.boundedElastic())
      .map(this.organizationMapper::organizationToPostDto);
  }

  private Organization mapOrgPostDtoToEntity(
    PostOrganizationRequest org,
    User owner
  ) {
    return new Organization(org.name(), owner);
  }
}
