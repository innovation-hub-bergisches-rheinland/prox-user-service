package de.innovationhub.prox.userservice.user.constraints;

import de.innovationhub.prox.userservice.user.service.UserIdentityService;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class IsValidUserIdConstraintValidator implements ConstraintValidator<IsValidUserId, UUID> {
  private final UserIdentityService userIdentityService;

  @Inject
  public IsValidUserIdConstraintValidator(UserIdentityService userIdentityService) {
    this.userIdentityService = userIdentityService;
  }

  @Override
  public boolean isValid(UUID value, ConstraintValidatorContext context) {
    return this.userIdentityService.existsById(value);
  }
}
