package de.innovationhub.prox.userservice.representative.repository;

import de.innovationhub.prox.userservice.representative.entity.Representative;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class RepresentativeRepositoryImpl implements RepresentativeRepository{
  private final RepresentativePanacheRepository panacheRepository;

  @Inject
  public RepresentativeRepositoryImpl(
      RepresentativePanacheRepository panacheRepository) {
    this.panacheRepository = panacheRepository;
  }

  @Override
  @Transactional
  public Optional<Representative> findById(UUID id) {
    return this.panacheRepository.findByIdOptional(id);
  }

  @Override
  @Transactional
  public List<Representative> findAll() {
    return this.panacheRepository.findAll().list();
  }

  @Override
  @Transactional
  public void save(Representative representative) {
    this.panacheRepository.persist(representative);
  }
}
