package de.innovationhub.prox.userservice.application.web;

import de.innovationhub.prox.userservice.application.service.OrganizationService;
import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.GetUserMembershipResponse;
import de.innovationhub.prox.userservice.domain.organization.dto.PostOrganizationRequest;
import de.innovationhub.prox.userservice.domain.organization.dto.PostOrganizationResponse;
import de.innovationhub.prox.userservice.domain.user.User;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

  @Autowired
  public OrganizationController(
    OrganizationService organizationService,
    ConversionService conversionService
  ) {
    this.organizationService = organizationService;
    this.conversionService = conversionService;
  }

  @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<ResponseEntity<GetOrganizationResponse>> getOrganizationById(
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
  @SecurityRequirement(
    name = "Bearer",
    scopes = "ROLE_organization_administrator"
  )
  public Mono<ResponseEntity<PostOrganizationResponse>> postOrganization(
    @RequestBody PostOrganizationRequest org,
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

  @GetMapping(
    value = "{id}/memberships",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<Set<GetUserMembershipResponse>>> getOrganizationMemberships(
    @PathVariable UUID id
  ) {
    return this.organizationService.findOrganizationMemberships(id)
      .collect(Collectors.toSet())
      .map(ResponseEntity::ok)
      .switchIfEmpty(
        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build())
      );
  }
}
