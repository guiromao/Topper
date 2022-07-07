package co.topper.domain.data.repository;

import co.topper.domain.data.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
