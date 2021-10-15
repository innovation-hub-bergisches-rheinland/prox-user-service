package de.innovationhub.prox.userservice.application.web;

import de.innovationhub.prox.userservice.application.service.UserService;
import de.innovationhub.prox.userservice.domain.organization.dto.GetOrganizationMembershipResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user")
@Slf4j
@SecurityRequirement(name = "Bearer")
public class AuthenticatedUserController {

  private final UserService userService;

  @Autowired
  public AuthenticatedUserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(
    value = "/memberships/orgs",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  public Mono<ResponseEntity<Set<GetOrganizationMembershipResponse>>> getOrganizationMemberships(
    @AuthenticationPrincipal Authentication authentication
  ) {
    return userService
      .findMembershipsOfAuthenticatedUser(authentication)
      .collect(Collectors.toSet())
      .map(ResponseEntity::ok);
  }
}
