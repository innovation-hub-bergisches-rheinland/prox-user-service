package de.innovationhub.prox.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

  @Bean
  public OpenAPI springDoc() {
    return new OpenAPI()
      .addServersItem(
        new Server()
          .url("https://api.prox.innovation-hub.de")
          .description("PROX Production API")
      )
      .addServersItem(
        new Server()
          .url("https://dev.api.prox.innovation-hub.de")
          .description("PROX Development API")
      )
      .info(
        new Info().title("PROX User Service").version("0.1.0") // TODO
      )
      .components(
        new Components()
          .addSecuritySchemes(
            "Bearer",
            new SecurityScheme()
              .type(Type.HTTP)
              .scheme("Bearer")
              .bearerFormat("JWT")
          )
      );
  }
}
