package co.topper.domain.data.repository;

import co.topper.domain.data.entity.TrackEntity;

import java.util.List;
import java.util.Optional;

public interface TrackRepositoryCustom {

    TrackEntity vote(String trackId, Long votes);

    List<TrackEntity> getTop(Integer offset, Integer limit);

    Optional<TrackEntity> getFeaturedTrack();

}
