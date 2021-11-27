package de.innovationhub.prox.userservice.application.service;

import de.innovationhub.prox.userservice.domain.user.User;
import de.innovationhub.prox.userservice.domain.user.UserRepository;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // Intentionally package-private so that it is not callable from controller
  Mono<User> getOrCreateAuthenticatedUser() {
    return ReactiveSecurityContextHolder.getContext()
        .map(ctx -> ctx.getAuthentication())
        .switchIfEmpty(Mono.error(new RuntimeException("Unauthenticated")))
        .map(auth -> UUID.fromString(auth.getName()))
        .flatMap(this::getOrCreateUserWithId);
  }

  @Transactional(TxType.REQUIRED)
  protected Mono<User> getOrCreateUserWithId(UUID id) {
    return Mono.fromCallable(() -> this.userRepository.findById(id))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(opt -> opt.map(Mono::just).orElseGet(Mono::empty))
        .switchIfEmpty(
            Mono.fromCallable(() -> this.userRepository.save(new User(id)))
                .subscribeOn(Schedulers.boundedElastic()));
  }
}
