package de.innovationhub.prox.userservice.infrastructure.iam.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;

public class KeycloakProvider {
  private final String keycloakServerUrl;
  private final String keycloakRealm;
  private final String keycloakClientId;
  private final String keycloakClientSecret;

  @Inject
  public KeycloakProvider(
      @ConfigProperty(name = "keycloak.server-url")
      String keycloakServerUrl,
      @ConfigProperty(name = "keycloak.realm")
      String keycloakRealm,
      @ConfigProperty(name = "keycloak.client-id")
      String keycloakClientId,
      @ConfigProperty(name = "keycloak.client-secret")
      String keycloakClientSecret) {
    this.keycloakServerUrl = keycloakServerUrl;
    this.keycloakRealm = keycloakRealm;
    this.keycloakClientId = keycloakClientId;
    this.keycloakClientSecret = keycloakClientSecret;
  }

  @ApplicationScoped
  public RealmResource keycloakRealmResource() {
    return KeycloakBuilder.builder()
        .serverUrl(this.keycloakServerUrl)
        .realm(this.keycloakRealm)
        .clientId(this.keycloakClientId)
        .clientSecret(this.keycloakClientSecret)
        .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        .build().realm(this.keycloakRealm);
  }
}
