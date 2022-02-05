package de.innovationhub.prox.userservice.application.organization.exception;

public class ForbiddenOrganizationAccessException extends RuntimeException {

  public ForbiddenOrganizationAccessException() {
    super("Forbidden Organization Access");
  }

  public ForbiddenOrganizationAccessException(String message) {
    super(message);
  }

  public ForbiddenOrganizationAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public ForbiddenOrganizationAccessException(Throwable cause) {
    super(cause);
  }
}
