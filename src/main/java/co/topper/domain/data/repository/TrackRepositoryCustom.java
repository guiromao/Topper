package co.topper.domain.data.repository;

import co.topper.domain.data.entity.TrackEntity;

public interface TrackRepositoryCustom {

    TrackEntity vote(String trackId, Long votes);

}
