package de.innovationhub.prox.userservice.organization.repository;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OrganizationPanacheRepository implements PanacheRepositoryBase<Organization, UUID> {

}
