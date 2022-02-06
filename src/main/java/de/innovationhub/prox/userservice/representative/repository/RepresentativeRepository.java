package de.innovationhub.prox.userservice.representative.repository;

import de.innovationhub.prox.userservice.representative.entity.Representative;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RepresentativeRepository {
  Optional<Representative> findById(UUID id);

  List<Representative> findAll();

  void save(Representative representative);
}
