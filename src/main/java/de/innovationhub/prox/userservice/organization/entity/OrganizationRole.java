package de.innovationhub.prox.userservice.organization.entity;

public enum OrganizationRole {
  MEMBER(0),
  ADMIN(1),
  OWNER(2);

  private int priority;

  OrganizationRole(int priority) {
    this.priority = priority;
  }

  public int getPriority() {
    return priority;
  }
}
