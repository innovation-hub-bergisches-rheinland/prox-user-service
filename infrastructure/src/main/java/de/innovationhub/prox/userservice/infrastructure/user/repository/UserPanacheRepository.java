package de.innovationhub.prox.userservice.infrastructure.user.repository;

import de.innovationhub.prox.userservice.infrastructure.user.jpa.UserJpa;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserPanacheRepository implements PanacheRepositoryBase<UserJpa, UUID> {

}
