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
  public Optional<User> findByIdOptional(UUID uuid) {
    return userPanacheRepository.findByIdOptional(uuid)
        .map(userJpaMapper::toDomain);
  }

  @Override
  @Transactional
  public void save(User user) {
    var jpaModel = userJpaMapper.toPersistence(user);
    userPanacheRepository.persist(jpaModel);
  }
}
