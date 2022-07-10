package co.topper.domain.data.repository;

import co.topper.domain.data.entity.UserEntity;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private static final String FIELD_ID = "_id";
    private static final String FIELD_TRACK_VOTES = "trackVotes";

    private final MongoTemplate mongoTemplate;

    public UserRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
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
        Update update = new Update().inc(FIELD_TRACK_VOTES + "." + trackId, votes);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);

        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, UserEntity.class);
    }

}
