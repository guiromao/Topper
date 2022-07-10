package co.topper.domain.data.repository;

import co.topper.domain.data.entity.UserEntity;
import org.springframework.data.mongodb.core.query.Update;

public interface UserRepositoryCustom {

    UserEntity updateUser(String userId, Update update);

    UserEntity updateVotes(String userId, String trackId, Long votes);

}
