package de.innovationhub.prox.userservice.user.constraints;

import de.innovationhub.prox.userservice.user.service.UserService;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@ApplicationScoped
public class IsValidUserIdConstraintValidator implements ConstraintValidator<IsValidUserId, UUID> {
  private final UserService userService;

  @Inject
  public IsValidUserIdConstraintValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public boolean isValid(UUID value, ConstraintValidatorContext context) {
    return this.userService.existsById(value);
  }
}
