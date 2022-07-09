package co.topper.domain.data.repository;

import co.topper.domain.data.entity.AlbumEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlbumRepository extends MongoRepository<AlbumEntity, String> {
}
