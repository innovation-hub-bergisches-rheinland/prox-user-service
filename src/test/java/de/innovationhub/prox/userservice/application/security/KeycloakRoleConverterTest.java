package de.innovationhub.prox.userservice.application.security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakRoleConverterTest {

  KeycloakRoleConverter keycloakRoleConverter;

  @BeforeEach
  void setup() {
    this.keycloakRoleConverter = new KeycloakRoleConverter();
  }

  @Test
  void shouldConvertKeycloakToken() {
    // Given
    var jwt = Jwt
      .withTokenValue("token")
      .header("alg", "none")
      .issuer("https://login.archi-lab.io/auth/realms/archilab")
      .claim(
        "realm_access",
        Map.of(
          "roles",
          List.of("professor", "offline_access", "uma_authorization")
        )
      )
      .claim(
        "resource_access",
        Map.of("prox-user-service", Map.of("roles", List.of("org-manager")))
      )
      .claim("scope", "openid email profile")
      .build();

    // When
    var result = this.keycloakRoleConverter.convert(jwt);

    // Then
    var expectedAuthorities = Set.of(
      new SimpleGrantedAuthority("ROLE_professor"),
      new SimpleGrantedAuthority("ROLE_offline_access"),
      new SimpleGrantedAuthority("ROLE_uma_authorization"),
      new SimpleGrantedAuthority("ROLE_org-manager"),
      new SimpleGrantedAuthority("SCOPE_openid"),
      new SimpleGrantedAuthority("SCOPE_email"),
      new SimpleGrantedAuthority("SCOPE_profile")
    );

    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
  }

  @Test
  void shouldConvertKeycloakTokenWithOnlyScopes() {
    // Given
    var jwt = Jwt
      .withTokenValue("token")
      .header("alg", "none")
      .issuer("https://login.archi-lab.io/auth/realms/archilab")
      .claim("scope", "openid email profile")
      .build();

    // When
    var result = this.keycloakRoleConverter.convert(jwt);

    // Then
    var expectedAuthorities = Set.of(
      new SimpleGrantedAuthority("SCOPE_openid"),
      new SimpleGrantedAuthority("SCOPE_email"),
      new SimpleGrantedAuthority("SCOPE_profile")
    );

    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
  }

  @Test
  void shouldConvertKeycloakTokenWithOnlyRealmAccess() {
    // Given
    var jwt = Jwt
      .withTokenValue("token")
      .header("alg", "none")
      .issuer("https://login.archi-lab.io/auth/realms/archilab")
      .claim(
        "realm_access",
        Map.of(
          "roles",
          List.of("professor", "offline_access", "uma_authorization")
        )
      )
      .build();

    // When
    var result = this.keycloakRoleConverter.convert(jwt);

    // Then
    var expectedAuthorities = Set.of(
      new SimpleGrantedAuthority("ROLE_professor"),
      new SimpleGrantedAuthority("ROLE_offline_access"),
      new SimpleGrantedAuthority("ROLE_uma_authorization")
    );

    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
  }

  @Test
  void shouldConvertKeycloakTokenWithOnlyResourceAccess() {
    // Given
    var jwt = Jwt
      .withTokenValue("token")
      .header("alg", "none")
      .issuer("https://login.archi-lab.io/auth/realms/archilab")
      .claim(
        "resource_access",
        Map.of("prox-user-service", Map.of("roles", List.of("org-manager")))
      )
      .build();

    // When
    var result = this.keycloakRoleConverter.convert(jwt);

    // Then
    var expectedAuthorities = Set.of(
      new SimpleGrantedAuthority("ROLE_org-manager")
    );

    assertThat(result).containsExactlyInAnyOrderElementsOf(expectedAuthorities);
  }
}
