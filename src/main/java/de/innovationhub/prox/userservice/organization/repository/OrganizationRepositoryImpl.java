package de.innovationhub.prox.userservice.organization.repository;

import de.innovationhub.prox.userservice.organization.entity.Organization;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.Record;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

@ApplicationScoped
public class OrganizationRepositoryImpl implements OrganizationRepository {
  private final Logger LOGGER = Logger.getLogger(OrganizationRepositoryImpl.class);
  private final OrganizationPanacheRepository organizationPanacheRepository;

  @Channel("organizations")
  Emitter<Record<String, Organization>> organizationEmitter;

  @Inject
  public OrganizationRepositoryImpl(OrganizationPanacheRepository organizationPanacheRepository) {
    this.organizationPanacheRepository = organizationPanacheRepository;
  }

  @Override
  @Transactional
  public Optional<Organization> findById(UUID id) {
    return organizationPanacheRepository.findByIdOptional(id);
  }

  @Override
  @Transactional
  public List<Organization> findAll() {
    return organizationPanacheRepository.findAll().list();
  }

  @Override
  @Transactional
  public List<Organization> findAllWithUserAsMember(UUID id) {
    return organizationPanacheRepository
        .find("from Organization o where key(o.members) = ?1", id)
        .list();
  }

  @Override
  @Transactional
  public void save(@Valid Organization organization) {
    this.organizationPanacheRepository.persist(organization);

    var orgEvent =
        organizationEmitter.send(Record.of(organization.getId().toString(), organization));
    Uni.createFrom()
        .completionStage(orgEvent)
        .onFailure()
        .invoke(
            e -> {
              LOGGER.errorf(
                  e,
                  "Error while sending organization event for organization '%s' to kafka",
                  organization.getId());
              QuarkusTransaction.setRollbackOnly();
            })
        .onItem()
        .invoke(
            () -> {
              LOGGER.debugf(
                  "Organization event for organization '%s' sent to kafka", organization.getId());
            })
        .await()
        .atMost(Duration.of(10, ChronoUnit.SECONDS));
  }
}
