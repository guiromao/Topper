package co.topper.domain.data.repository;

import co.topper.domain.data.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String>, UserRepositoryCustom {

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

}
