package de.innovationhub.prox.userservice.domain.user;

import de.innovationhub.prox.userservice.domain.organization.Membership;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
  @Query("select u.members from User u where u.id = :id")
  Set<Membership> findMembershipsOfUserWithId(@Param("id") UUID id);
}
