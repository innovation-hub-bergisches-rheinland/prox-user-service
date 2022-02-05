package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.core.user.UserId;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative.RepresentativeId;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RepresentativeJpaMapperTest {

  private RepresentativeJpaMapper mapper = RepresentativeJpaMapper.INSTANCE;

  @Test
  void toDomain() {
    // Given
    var persistence = RepresentativeJpaEntity.builder()
        .id(UUID.randomUUID())
        .owner(UUID.randomUUID())
        .name("Prof. Dr. Max Mustermann")
        .build();

    // When
    var domain = mapper.toDomain(persistence);

    // Then
    assertThat(domain.getName()).isEqualTo("Prof. Dr. Max Mustermann");
    assertThat(domain.getId().id()).isEqualTo(persistence.getId());
    assertThat(domain.getUser().id()).isEqualTo(persistence.getOwner());
  }

  @Test
  void toPersistence() {
    // Given
    var domain = new Representative(new RepresentativeId(UUID.randomUUID()), new UserId(UUID.randomUUID()), "Prof. Dr. Max Mustermann");

    // When
    var jpa = mapper.toPersistence(domain);

    // Then
    assertThat(jpa.getId()).isEqualTo(domain.getId().id());
    assertThat(jpa.getName()).isEqualTo("Prof. Dr. Max Mustermann");
    assertThat(jpa.getOwner()).isEqualTo(domain.getUser().id());
  }
}
