package de.innovationhub.prox.userservice.infrastructure.user.repository;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import de.innovationhub.prox.userservice.infrastructure.user.mapper.UserJpaMapper;
import java.util.Optional;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class UserRepositoryImpl implements UserRepository {

  private final UserPanacheRepository userPanacheRepository;
  private final UserJpaMapper userJpaMapper;

  public UserRepositoryImpl(
      UserPanacheRepository userPanacheRepository,
      UserJpaMapper userJpaMapper) {
    this.userPanacheRepository = userPanacheRepository;
    this.userJpaMapper = userJpaMapper;
  }

  @Override
  public Optional<User> findByPrincipalOptional(String principal) {
    return userPanacheRepository.find("principal", principal).firstResultOptional()
        .map(userJpaMapper::toDomain);
  }

  @Override
  public boolean existByPrincipal(String principal) {
    return userPanacheRepository.find("principal", principal).count() > 0;
  }

  @Override
  @Transactional
  public void create(User user) {
    var jpaModel = userJpaMapper.toPersistence(user);
    userPanacheRepository.persist(jpaModel);
  }

  @Override
  @Transactional
  public void update(String principal, User user) {
    var jpa = userPanacheRepository.find("principal", principal).firstResultOptional()
        .map(userJpa -> userJpaMapper.toPersistence(userJpa.getId(), user))
        .orElseThrow();
    userPanacheRepository.persist(jpa);
  }
}
