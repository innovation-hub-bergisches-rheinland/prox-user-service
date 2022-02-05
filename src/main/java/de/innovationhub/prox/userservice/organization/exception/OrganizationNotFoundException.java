package de.innovationhub.prox.userservice.organization.exception;

public class OrganizationNotFoundException extends RuntimeException {

  public OrganizationNotFoundException() {
    super("Organization not found");
  }

  public OrganizationNotFoundException(String message) {
    super(message);
  }

  public OrganizationNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public OrganizationNotFoundException(Throwable cause) {
    super(cause);
  }
}
