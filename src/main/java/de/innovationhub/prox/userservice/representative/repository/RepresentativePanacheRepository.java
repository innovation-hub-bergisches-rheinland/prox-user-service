package de.innovationhub.prox.userservice.representative.repository;

import de.innovationhub.prox.userservice.representative.entity.Representative;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RepresentativePanacheRepository
    implements PanacheRepositoryBase<Representative, UUID> {}
