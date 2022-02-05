package de.innovationhub.prox.userservice.infrastructure.persistence.representative;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.representative.enitity.Representative;
import de.innovationhub.prox.userservice.domain.representative.enitity.Representative.RepresentativeId;
import de.innovationhub.prox.userservice.domain.user.UserId;
import java.util.UUID;
import org.assertj.core.api.Assertions;
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
    Assertions.assertThat(domain.getName()).isEqualTo("Prof. Dr. Max Mustermann");
    Assertions.assertThat(domain.getId().id()).isEqualTo(persistence.getId());
    Assertions.assertThat(domain.getUser().id()).isEqualTo(persistence.getOwner());
  }

  @Test
  void toPersistence() {
    // Given
    var domain = new Representative(new RepresentativeId(UUID.randomUUID()), new UserId(UUID.randomUUID()), "Prof. Dr. Max Mustermann");

    // When
    var jpa = mapper.toPersistence(domain);

    // Then
    Assertions.assertThat(jpa.getId()).isEqualTo(domain.getId().id());
    Assertions.assertThat(jpa.getName()).isEqualTo("Prof. Dr. Max Mustermann");
    Assertions.assertThat(jpa.getOwner()).isEqualTo(domain.getUser().id());
  }
}
