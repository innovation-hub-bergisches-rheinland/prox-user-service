package de.innovationhub.prox.userservice.domain.user.exception;

public class AmbiguousPrincipalException extends RuntimeException {

  public AmbiguousPrincipalException() {
    super("Principal already exists");
  }

  public AmbiguousPrincipalException(String message) {
    super(message);
  }
}
