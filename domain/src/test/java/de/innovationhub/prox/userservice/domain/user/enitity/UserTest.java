package de.innovationhub.prox.userservice.domain.user.enitity;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationMembership;
import de.innovationhub.prox.userservice.domain.user.vo.OrganizationRole;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserTest {
  @Test
  void should_throw_exception_when_principal_is_null() {
    assertThatThrownBy(() -> new User(null)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_add_membership_when_user_is_not_a_member() {
    // Given
    var user = new User("max-mustermann");
    var organization = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG");
    var organizationMembership = new OrganizationMembership(OrganizationRole.OWNER);

    // When
    user.addMembership(organization, organizationMembership);

    // Then
    var membership = user.getMemberships().get(organization);
    assertThat(membership).isNotNull();
    assertThat(membership.role()).isEqualTo(OrganizationRole.OWNER);
  }

  @Test
  void should_throw_exception_when_user_is_a_member_and_membership_is_added() {
    // Given
    var user = new User("max-mustermann");
    var organization = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG");
    var organizationMembership = new OrganizationMembership(OrganizationRole.OWNER);
    user.addMembership(organization, organizationMembership);

    // When / Then
    assertThatThrownBy(() -> user.addMembership(organization, organizationMembership)).isInstanceOf(RuntimeException.class);
  }

  @Test
  void should_update_membership_when_user_is_a_member() {
    // Given
    var user = new User("max-mustermann");
    var organization = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG");
    var organizationMembership = new OrganizationMembership(OrganizationRole.OWNER);
    user.addMembership(organization, organizationMembership);

    // When
    user.updateMembership(organization, new OrganizationMembership(OrganizationRole.MEMBER));

    // Then
    var membership = user.getMemberships().get(organization);
    assertThat(membership).isNotNull();
    assertThat(membership.role()).isEqualTo(OrganizationRole.MEMBER);
  }

  @Test
  void should_throw_exception_when_user_is_not_a_member_and_membership_is_updated() {
    // Given
    var user = new User("max-mustermann");
    var organization = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG");
    var organizationMembership = new OrganizationMembership(OrganizationRole.OWNER);

    // When / Then
    assertThatThrownBy(() -> user.updateMembership(organization, new OrganizationMembership(OrganizationRole.MEMBER))).isInstanceOf(RuntimeException.class);
  }

  @Test
  void should_remove_membership() {
    // Given
    var user = new User("max-mustermann");
    var organization = new Organization(UUID.randomUUID(), "Musterfirma GmbH & Co. KG");
    var organizationMembership = new OrganizationMembership(OrganizationRole.OWNER);
    user.addMembership(organization, organizationMembership);

    // When
    user.removeMembership(organization);

    // Then
    var membership = user.getMemberships().get(organization);
    assertThat(membership).isNull();
  }
}