package de.innovationhub.prox.userservice.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

  private final ServerHttpSecurity http;

  @Autowired
  public SecurityConfig(ServerHttpSecurity security) {
    this.http = security;
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain() {
    return http.csrf()
        .disable()
        .formLogin()
        .disable()
        .httpBasic()
        .disable()
        .logout()
        .disable()
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> Customizer.withDefaults()))
        .authorizeExchange(
            exchange ->
                exchange
                    .pathMatchers(HttpMethod.GET, "/orgs/{id}")
                    .permitAll()
                    .pathMatchers(HttpMethod.POST, "/orgs")
                    .authenticated())
        .build();
  }
}
