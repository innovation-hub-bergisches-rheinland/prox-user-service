package de.innovationhub.prox.userservice.infrastructure.rest.organization;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class OidcWiremockTestProfile implements QuarkusTestProfile {

  @Override
  public Map<String, String> getConfigOverrides() {
    return Map.of("quarkus.oidc.auth-server-url", "${keycloak.url}/realms/quarkus");
  }
}
