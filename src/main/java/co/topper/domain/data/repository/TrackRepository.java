package co.topper.domain.data.repository;

import co.topper.domain.data.entity.TrackEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackRepository extends MongoRepository<TrackEntity, String> {
}
