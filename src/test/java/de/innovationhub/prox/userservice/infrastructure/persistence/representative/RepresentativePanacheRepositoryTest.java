package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import de.innovationhub.prox.userservice.representative.entity.Representative;
import de.innovationhub.prox.userservice.representative.repository.RepresentativePanacheRepository;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
class RepresentativePanacheRepositoryTest {

  @Inject
  RepresentativePanacheRepository representativePanacheRepository;

  @Test
  @Disabled("No pre-populated data source introduced")
  void findByIdOptional() {
    // TODO: In order to test this, we should introduce a populated database
    // Given
    var repId = UUID.fromString("00000000-0000-0000-0000-00000000");

    // When
    var optFound = representativePanacheRepository.findByIdOptional(repId);

    // Then
    Assertions.assertThat(optFound).isNotEmpty();
  }

  @Test
  void save() {
    // Given
    var repId = UUID.randomUUID();
    var user = UUID.randomUUID();
    var rep = new Representative(repId, user, "Prof. Dr. Max Mustermann");

    // When
    representativePanacheRepository.persist(rep);

    // Then
  }
}
