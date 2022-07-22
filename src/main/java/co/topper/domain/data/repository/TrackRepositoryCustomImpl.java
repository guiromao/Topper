package co.topper.domain.data.repository;

import co.topper.domain.data.entity.TrackEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;

public class TrackRepositoryCustomImpl implements TrackRepositoryCustom {

    private static final String KEY_ID = "_id";
    private static final String KEY_VOTES = "votes";

    private final MongoTemplate mongoTemplate;

    public TrackRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public TrackEntity vote(String trackId, Long votes) {
        Query query = Query.query(Criteria.where(KEY_ID).is(trackId));
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
        Update update = new Update().inc(KEY_VOTES, votes);

        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, TrackEntity.class);
    }

    @Override
    public List<TrackEntity> getTop(Pageable page) {
        return mongoTemplate.find(
                new Query(Criteria.where(KEY_ID).ne(TrackEntity.FEATURED_TRACK_ID))
                        .with(Sort.by(Direction.DESC, KEY_VOTES)).with(page),
                TrackEntity.class
        );
    }

    @Override
    public Optional<TrackEntity> getFeaturedTrack() {
        return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where(KEY_ID).is(TrackEntity.FEATURED_TRACK_ID)),
                TrackEntity.class));
    }

}
