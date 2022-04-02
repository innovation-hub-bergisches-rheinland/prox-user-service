package de.innovationhub.prox.userservice.user.constraints;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.user.service.UserService;
import java.util.UUID;
import javax.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IsValidUserIdConstraintValidatorTest {

  IsValidUserIdConstraintValidator validator;

  @Mock UserService userService;

  @Mock ConstraintValidatorContext constraintValidatorContext;

  @BeforeEach
  void setup() {
    this.validator = new IsValidUserIdConstraintValidator(userService);
  }

  @Test
  void shouldReturnTrueIfUserExists() {
    // Given
    when(userService.existsById(any())).thenReturn(true);
    assertTrue(this.validator.isValid(UUID.randomUUID(), constraintValidatorContext));
  }

  @Test
  void shouldReturnFalseIfUserDoesntExists() {
    // Given
    when(userService.existsById(any())).thenReturn(false);
    assertFalse(this.validator.isValid(UUID.randomUUID(), constraintValidatorContext));
  }
}
