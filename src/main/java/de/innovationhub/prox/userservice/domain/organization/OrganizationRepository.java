package de.innovationhub.prox.userservice.domain.organization;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationRepository implements PanacheRepositoryBase<OrganizationJpa, UUID> {

}
