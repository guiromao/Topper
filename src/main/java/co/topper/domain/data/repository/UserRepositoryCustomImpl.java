package co.topper.domain.data.repository;

import co.topper.configuration.RedisConfiguration;
import co.topper.domain.data.entity.UserEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static co.topper.configuration.constants.UserConstants.FIELD_AVAILABLE_VOTES;

import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private static final String FIELD_ID = "_id";
    private static final String FIELD_TRACK_VOTES = "trackVotes";
    private static final String FIELD_EMAIL = "email";

    private final MongoTemplate mongoTemplate;

    public UserRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Optional<UserEntity> findByEmailNoCache(String email) {
        Criteria emailCriteria = Criteria.where(FIELD_EMAIL).is(email);

        return Optional.ofNullable(mongoTemplate.findOne(new Query(emailCriteria), UserEntity.class));
    }

    @Override
    @Cacheable(value = RedisConfiguration.CACHE_USER_EMAILS)
    public Optional<UserEntity> findByEmail(String email) {
        return findByEmailNoCache(email);
    }

    @Override
    public UserEntity updateUser(String userId, Update update) {
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
        Query query = new Query(Criteria.where(FIELD_ID).is(userId));

        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

    @Override
    public UserEntity updateVotes(String userId, String trackId, Long votes) {
        Query query = Query.query(Criteria.where(FIELD_ID).is(userId));
        Update update = new Update().inc(FIELD_TRACK_VOTES + "." + trackId, votes)
                .inc(FIELD_AVAILABLE_VOTES, -votes);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);

        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }


}
