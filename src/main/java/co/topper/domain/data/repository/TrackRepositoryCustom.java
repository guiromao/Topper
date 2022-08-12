package co.topper.domain.data.repository;

import co.topper.domain.data.entity.TrackEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TrackRepositoryCustom {

    TrackEntity vote(String trackId, Long votes);

    List<TrackEntity> getTop(Integer page);

    Optional<TrackEntity> getFeaturedTrack();

}
