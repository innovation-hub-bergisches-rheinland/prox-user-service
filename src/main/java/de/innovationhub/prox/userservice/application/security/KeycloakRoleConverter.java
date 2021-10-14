package de.innovationhub.prox.userservice.application.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class KeycloakRoleConverter
  implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt) {
    final var realmAccessClaim = jwt.getClaimAsMap("realm_access");
    final var resourceAccessClaim = jwt.getClaimAsMap("resource_access");
    final var scopeClaim = jwt.getClaimAsString("scope");

    Stream<String> rolesStream = Stream.empty();
    Stream<String> scopeStream = Stream.empty();

    if (realmAccessClaim != null) {
      final var realmAccessRoles = (List<String>) realmAccessClaim.get("roles");
      if (realmAccessRoles != null) {
        rolesStream = Stream.concat(rolesStream, realmAccessRoles.stream());
      }
    }

    if (resourceAccessClaim != null) {
      final var resourceClaim = (Map<String, Object>) resourceAccessClaim.get(
        "prox-user-service"
      );
      if (resourceClaim != null) {
        final var resourceAccessRoles = (List<String>) resourceClaim.get(
          "roles"
        ); // TODO: Hardcoded claim name, may be outsourced into config
        if (resourceAccessRoles != null) {
          rolesStream =
            Stream.concat(rolesStream, resourceAccessRoles.stream());
        }
      }
    }

    if (scopeClaim != null) {
      scopeStream = Arrays.stream(scopeClaim.split("\\s+"));
    }

    final var rolesAuthoritiesStream = rolesStream
      .map(roleName -> "ROLE_" + roleName)
      .map(SimpleGrantedAuthority::new);

    final var scopeAuthoritiesStream = scopeStream
      .map(scope -> "SCOPE_" + scope)
      .map(SimpleGrantedAuthority::new);

    return Stream
      .concat(rolesAuthoritiesStream, scopeAuthoritiesStream)
      .collect(Collectors.toSet());
  }
}
