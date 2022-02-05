package de.innovationhub.prox.userservice.domain.representative.repository;

import de.innovationhub.prox.userservice.domain.representative.enitity.Representative;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative.RepresentativeId;
import java.util.Optional;

public interface RepresentativeRepository {
  Optional<Representative> findByIdOptional(RepresentativeId id);
  void save(Representative representative);
}
