package de.innovationhub.prox.userservice.user.constraints;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import de.innovationhub.prox.userservice.user.service.KeycloakService;
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

  @Mock KeycloakService keycloakService;

  @Mock ConstraintValidatorContext constraintValidatorContext;

  @BeforeEach
  void setup() {
    this.validator = new IsValidUserIdConstraintValidator(keycloakService);
  }

  @Test
  void shouldReturnTrueIfUserExists() {
    // Given
    when(keycloakService.existsById(any())).thenReturn(true);
    assertTrue(this.validator.isValid(UUID.randomUUID(), constraintValidatorContext));
  }

  @Test
  void shouldReturnFalseIfUserDoesntExists() {
    // Given
    when(keycloakService.existsById(any())).thenReturn(false);
    assertFalse(this.validator.isValid(UUID.randomUUID(), constraintValidatorContext));
  }
}
