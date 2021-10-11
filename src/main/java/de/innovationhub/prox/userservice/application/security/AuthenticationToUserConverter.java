package de.innovationhub.prox.userservice.application.security;

import de.innovationhub.prox.userservice.domain.user.User;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationToUserConverter
  implements Converter<Authentication, User> {

  private final UserRepository userRepository;

  public AuthenticationToUserConverter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  @Transactional(TxType.REQUIRED)
  public User convert(Authentication source) {
    try {
      var id = UUID.fromString(source.getName());
      return getOrCreateUserWithId(id);
    } catch (IllegalArgumentException e) {
      log.error(
        "Could not extract user from authentication " +
        source +
        " because the name is not a valid uuid",
        e
      );
    }
    return null;
  }

  /*
   * TODO: This is not a clean way to achieve synchronization of users between
   * Keycloak and this service, but as long as we have no eventing and no
   * Kafka Stream required :(
   */
  private User getOrCreateUserWithId(UUID id) {
    return this.userRepository.findById(id)
      .orElse(this.userRepository.save(new User(id)));
  }
}
