package de.innovationhub.prox.userservice.infrastructure.iam.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "keycloak")
public interface KeycloakConfiguration {
  String serverUrl();

  String realm();

  String clientId();

  String clientSecret();
}
