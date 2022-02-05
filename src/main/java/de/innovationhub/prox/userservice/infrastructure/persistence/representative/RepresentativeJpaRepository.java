package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import de.innovationhub.prox.userservice.domain.representative.enitity.Representative;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative.RepresentativeId;
import de.innovationhub.prox.userservice.domain.representative.repository.RepresentativeRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class RepresentativeJpaRepository implements PanacheRepositoryBase<RepresentativeJpaEntity, UUID>,
    RepresentativeRepository
{

  private final RepresentativeJpaMapper mapper;

  @Inject
  public RepresentativeJpaRepository(
      RepresentativeJpaMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public Optional<Representative> findByIdOptional(RepresentativeId id) {
    return this.findByIdOptional(id.id())
        .map(mapper::toDomain);
  }

  @Override
  @Transactional
  public void save(Representative representative) {
    var repToSave = mapper.toPersistence(representative);
    this.persist(repToSave);
  }
}
