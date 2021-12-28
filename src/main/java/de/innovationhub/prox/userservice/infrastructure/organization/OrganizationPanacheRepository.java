package de.innovationhub.prox.userservice.infrastructure.organization;

import de.innovationhub.prox.userservice.domain.organization.OrganizationJpa;
import de.innovationhub.prox.userservice.domain.organization.OrganizationRepository;
import de.innovationhub.prox.userservice.domain.user.UserJpa;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationPanacheRepository implements OrganizationRepository, PanacheRepositoryBase<OrganizationJpa, UUID> {

}
