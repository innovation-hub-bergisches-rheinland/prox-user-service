package de.innovationhub.prox.userservice.infrastructure.user;

import de.innovationhub.prox.userservice.domain.user.UserJpa;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserPanacheRepository implements UserRepository, PanacheRepositoryBase<UserJpa, UUID> {

}
