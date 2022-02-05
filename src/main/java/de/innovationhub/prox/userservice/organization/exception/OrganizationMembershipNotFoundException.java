package de.innovationhub.prox.userservice.organization.exception;

public class OrganizationMembershipNotFoundException extends RuntimeException {

  public OrganizationMembershipNotFoundException() {
    super("Organization membership not found");
  }

  public OrganizationMembershipNotFoundException(String message) {
    super(message);
  }

  public OrganizationMembershipNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public OrganizationMembershipNotFoundException(Throwable cause) {
    super(cause);
  }
}
