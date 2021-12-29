package de.innovationhub.prox.userservice.infrastructure.membership.repository;

import de.innovationhub.prox.userservice.infrastructure.membership.jpa.OrganizationMembershipJpa;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationMembershipPanacheRepository implements PanacheRepositoryBase<OrganizationMembershipJpa, UUID> {

}
