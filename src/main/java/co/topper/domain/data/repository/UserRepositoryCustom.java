package co.topper.domain.data.repository;

import co.topper.domain.data.entity.UserEntity;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Optional;

public interface UserRepositoryCustom {

    UserEntity updateUser(String userEmail, Update update);

}
