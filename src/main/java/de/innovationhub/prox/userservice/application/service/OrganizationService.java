package de.innovationhub.prox.userservice.application.service;

import de.innovationhub.prox.userservice.domain.organization.Organization;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationGetDto;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationPostDto;
import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
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

  public OrganizationService(
      OrganizationRepository organizationRepository,
      UserService userService) {
    this.organizationRepository = organizationRepository;
    this.userService = userService;
  }

  public Mono<OrganizationGetDto> getOrganizationWithId(UUID id) {
    return Mono.fromCallable(() -> organizationRepository.findById(id))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(optional -> optional.map(Mono::just).orElseGet(Mono::empty))
        .map(this::mapOrgToGetDto);
  }

  @Transactional(TxType.REQUIRED)
  public Mono<OrganizationGetDto> createOrganization(OrganizationPostDto org) {
    return userService.getOrCreateAuthenticatedUser()
        .switchIfEmpty(Mono.error(new RuntimeException("Could not retrieve authenticated user")))
        .flatMap(user -> Mono.fromCallable(
            () -> organizationRepository.save(mapOrgPostDtoToEntity(org, user))))
        .subscribeOn(Schedulers.boundedElastic())
        .map(this::mapOrgToGetDto);
  }

  // TODO: automatic, but still typesafe mapping (Maybe mapstruct? :thonk)
  private OrganizationGetDto mapOrgToGetDto(Organization org) {
    return new OrganizationGetDto(org.getId(), org.getName());
  }

  private Organization mapOrgPostDtoToEntity(OrganizationPostDto org, User owner) {
    return new Organization(org.name(), owner);
  }
}
