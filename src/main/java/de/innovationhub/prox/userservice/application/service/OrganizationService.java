package de.innovationhub.prox.userservice.application.service;

import de.innovationhub.prox.userservice.domain.organization.Organization;
import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationGetDto;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationMapper;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationPostDto;
import de.innovationhub.prox.userservice.domain.user.User;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final UserService userService;
  private final OrganizationMapper organizationMapper;

  public OrganizationService(
    OrganizationRepository organizationRepository,
    UserService userService,
    OrganizationMapper organizationMapper
  ) {
    this.organizationRepository = organizationRepository;
    this.userService = userService;
    this.organizationMapper = organizationMapper;
  }

  public Mono<OrganizationGetDto> getOrganizationWithId(UUID id) {
    return Mono
      .fromCallable(() -> organizationRepository.findById(id))
      .subscribeOn(Schedulers.boundedElastic())
      .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
      .map(this.organizationMapper::organizationToGetDto);
  }

  @Transactional(TxType.REQUIRED)
  public Mono<OrganizationGetDto> createOrganization(OrganizationPostDto org) {
    return userService
      .getOrCreateAuthenticatedUser()
      .switchIfEmpty(
        Mono.error(
          new RuntimeException("Could not retrieve authenticated user")
        )
      )
      .flatMap(user ->
        Mono.fromCallable(() ->
          organizationRepository.save(mapOrgPostDtoToEntity(org, user))
        )
      )
      .subscribeOn(Schedulers.boundedElastic())
      .map(this.organizationMapper::organizationToGetDto);
  }

  private Organization mapOrgPostDtoToEntity(
    OrganizationPostDto org,
    User owner
  ) {
    return new Organization(org.name(), owner);
  }
}
