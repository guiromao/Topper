package co.topper.domain.data.repository;

import co.topper.domain.data.entity.ArtistEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArtistRepository extends MongoRepository<ArtistEntity, String> {
}
