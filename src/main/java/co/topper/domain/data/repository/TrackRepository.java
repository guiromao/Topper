package co.topper.domain.data.repository;

import co.topper.domain.data.entity.Track;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackRepository extends MongoRepository<Track, String> {
}
