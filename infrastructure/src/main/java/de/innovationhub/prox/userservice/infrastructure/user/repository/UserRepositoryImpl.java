package de.innovationhub.prox.userservice.infrastructure.user.repository;

import de.innovationhub.prox.userservice.domain.user.enitity.User;
import de.innovationhub.prox.userservice.domain.user.repository.UserRepository;
import de.innovationhub.prox.userservice.infrastructure.user.mapper.UserJpaMapper;
import java.util.Optional;
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
        .map(userJpaMapper::toDomainEntity);
  }

  @Override
  public boolean existByPrincipal(String principal) {
    return userPanacheRepository.find("principal", principal).count() > 0;
  }

  @Override
  public void create(User user) {
    var jpaModel = userJpaMapper.createJpaEntity(user);
    userPanacheRepository.persist(jpaModel);
  }
}
