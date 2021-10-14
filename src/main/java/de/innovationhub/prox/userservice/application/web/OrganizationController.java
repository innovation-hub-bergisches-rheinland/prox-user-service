package de.innovationhub.prox.userservice.application.web;

import de.innovationhub.prox.userservice.application.service.OrganizationService;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationGetDto;
import de.innovationhub.prox.userservice.domain.organization.dto.OrganizationPostDto;
import de.innovationhub.prox.userservice.domain.user.User;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orgs")
public class OrganizationController {

  private final OrganizationService organizationService;
  private final ConversionService conversionService;
  private final SecurityWebFilterChain securityWebFilterChain;

  @Autowired
  public OrganizationController(
    OrganizationService organizationService,
    ConversionService conversionService,
    SecurityWebFilterChain securityWebFilterChain
  ) {
    this.organizationService = organizationService;
    this.conversionService = conversionService;
    this.securityWebFilterChain = securityWebFilterChain;
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<OrganizationGetDto>> getOrganizationById(
    @PathVariable UUID id
  ) {
    return organizationService
      .getOrganizationWithId(id)
      .map(ResponseEntity::ok)
      .switchIfEmpty(
        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
      );
  }

  @PostMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<OrganizationGetDto>> postOrganization(
    @RequestBody OrganizationPostDto org,
    @AuthenticationPrincipal Authentication authentication
  ) {
    // Get authenticated user as entity
    var user = conversionService.convert(authentication, User.class);
    return organizationService
      .createOrganization(org, user)
      .map(createdOrg ->
        ResponseEntity.status(HttpStatus.CREATED).body(createdOrg)
      );
  }
}
