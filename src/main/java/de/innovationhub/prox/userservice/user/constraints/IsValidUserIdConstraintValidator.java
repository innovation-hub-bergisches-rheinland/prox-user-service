package de.innovationhub.prox.userservice.user.constraints;

import de.innovationhub.prox.userservice.user.service.KeycloakService;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class IsValidUserIdConstraintValidator implements ConstraintValidator<IsValidUserId, UUID> {
  private final KeycloakService keycloakService;

  @Inject
  public IsValidUserIdConstraintValidator(KeycloakService keycloakService) {
    this.keycloakService = keycloakService;
  }

  @Override
  public boolean isValid(UUID value, ConstraintValidatorContext context) {
    return this.keycloakService.existsById(value);
  }
}
