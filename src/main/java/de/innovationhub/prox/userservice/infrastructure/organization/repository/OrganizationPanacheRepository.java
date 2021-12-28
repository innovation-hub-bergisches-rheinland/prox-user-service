package de.innovationhub.prox.userservice.infrastructure.organization.repository;

import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class OrganizationPanacheRepository implements PanacheRepositoryBase<OrganizationJpa, UUID> {
}
