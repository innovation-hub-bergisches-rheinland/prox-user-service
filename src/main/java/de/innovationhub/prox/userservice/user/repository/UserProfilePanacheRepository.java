package de.innovationhub.prox.userservice.user.repository;

import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProfilePanacheRepository implements PanacheRepositoryBase<UserProfile, UUID> {}
