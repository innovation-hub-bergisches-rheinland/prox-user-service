package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.core.user.entity.ProxUser;
import de.innovationhub.prox.userservice.domain.organization.entity.Organization.OrganizationId;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative.RepresentativeId;
import io.quarkus.oidc.common.runtime.OidcCommonConfig.Credentials.Jwt;
import io.quarkus.test.junit.QuarkusTest;
import java.util.UUID;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@QuarkusTest
@Transactional
class RepresentativeJpaRepositoryTest {

  @Inject
  RepresentativeJpaRepository representativeJpaRepository;

  @Test
  @Disabled("No pre-populated data source introduced")
  void findByIdOptional() {
    // TODO: In order to test this, we should introduce a populated database
    // Given
    var repId = new RepresentativeId(UUID.fromString("00000000-0000-0000-0000-00000000"));

    // When
    var optFound = representativeJpaRepository.findByIdOptional(repId);

    // Then
    assertThat(optFound).isNotEmpty();
  }

  @Test
  void save() {
    // Given
    var repId = new RepresentativeId(UUID.randomUUID());
    var user = new ProxUser(UUID.randomUUID().toString());
    var rep = new Representative(repId, user, "Prof. Dr. Max Mustermann");

    // When
    representativeJpaRepository.save(rep);

    // Then
  }
}