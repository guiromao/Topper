package co.topper.domain.data.repository;

import co.topper.domain.data.entity.UserEntity;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private static final String FIELD_ID = "_id";

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

}
