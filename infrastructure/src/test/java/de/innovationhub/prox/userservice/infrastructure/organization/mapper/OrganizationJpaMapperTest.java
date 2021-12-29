package de.innovationhub.prox.userservice.infrastructure.organization.mapper;

import static org.assertj.core.api.Assertions.*;

import de.innovationhub.prox.userservice.domain.organization.entity.Organization;
import de.innovationhub.prox.userservice.infrastructure.organization.jpa.OrganizationJpa;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrganizationJpaMapperTest {

  private OrganizationJpaMapper jpaMapper;

  @BeforeEach
  void setUp() {
    this.jpaMapper = OrganizationJpaMapper.INSTANCE;
  }

  @Test
  void should_map_Organization_to_OrganizationJpa() {
    // Given
    var id = UUID.randomUUID();
    var organization = new Organization(id, "Musterfirma GmbH & Co. KG");

    // When
    var jpa = jpaMapper.toPersistence(organization);

    // Then
    assertThat(jpa).isNotNull();
    assertThat(jpa.getId()).isEqualTo(id);
    assertThat(jpa.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
  }

  @Test
  void should_map_OrganizationJpa_to_Organization() {
    // Given
    var id = UUID.randomUUID();
    var jpa = new OrganizationJpa();
    jpa.setId(id);
    jpa.setName("Musterfirma GmbH & Co. KG");

    // When
    var organization = jpaMapper.toDomain(jpa);

    // Then
    assertThat(organization).isNotNull();
    assertThat(organization.getId()).isEqualTo(id);
    assertThat(organization.getName()).isEqualTo("Musterfirma GmbH & Co. KG");
  }
}