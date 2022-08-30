package de.innovationhub.prox.userservice.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.innovationhub.prox.userservice.user.entity.User;
import de.innovationhub.prox.userservice.user.entity.profile.UserProfile;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import java.util.UUID;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
class UserRepositoryTest {
  @InjectKafkaCompanion KafkaCompanion kc;

  @Inject UserRepository userRepository;

  @Inject ObjectMapper objectMapper;

  @Test
  void shouldPublishOnSave() throws Exception {
    var user = new User(UUID.randomUUID(), "name", "email");
    var profile =
        new UserProfile(
            user.getId(), "name", "affilation", "subject", null, null, "Lorem Ipsum", null);
    user.setProfile(profile);

    // When
    userRepository.save(user);

    // Then
    var records =
        kc.consume(String.class, String.class).fromTopics("entity.user.user").awaitRecords(1);
    assertThat(records).hasSize(1);
    var record = records.getFirstRecord();

    // Key assertion for log compaction
    assertThat(record.key()).isEqualTo(user.getId().toString());

    // Value assertion to ensure that the entity state is published
    var publishedUser = objectMapper.readValue(record.value(), User.class);
    assertThat(publishedUser).isEqualTo(user);
  }
}
