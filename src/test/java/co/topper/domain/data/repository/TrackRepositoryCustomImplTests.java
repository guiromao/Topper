package co.topper.domain.data.repository;

import co.topper.domain.data.entity.TrackEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TrackRepositoryCustomImplTests {

    @Mock
    MongoTemplate mongoTemplate;

    TrackRepositoryCustomImpl trackRepositoryCustom;

    @BeforeEach
    void setup() {
        trackRepositoryCustom = new TrackRepositoryCustomImpl(mongoTemplate);
    }

    @Test
    void testVote() {
        when(mongoTemplate.findAndModify(any(Query.class), any(Update.class),
                any(FindAndModifyOptions.class), any(Class.class)))
                        .thenReturn(mock(TrackEntity.class));

        trackRepositoryCustom.vote("track-1", 1000L);

        Mockito.verify(mongoTemplate, times(1))
                .findAndModify(any(Query.class), any(Update.class),
                        any(FindAndModifyOptions.class), any(Class.class));
    }

    @Test
    void testGetTop() {
        List<TrackEntity> tracks = List.of(trackWithId("track-1"), trackWithId("track-2"));

        when(mongoTemplate.find(any(Query.class), any(Class.class))).thenReturn(tracks);

        List<TrackEntity> test = trackRepositoryCustom.getTop(0, 5);

        verify(mongoTemplate, times(1)).find(any(Query.class), any(Class.class));
        Assertions.assertEquals(tracks.size(), test.size());
    }

    @Test
    void testGetFeaturedTrack() {
        TrackEntity track = trackWithId("track-1");

        when(mongoTemplate.findOne(any(Query.class), any(Class.class))).thenReturn(track);

        Optional<TrackEntity> test = trackRepositoryCustom.getFeaturedTrack();

        Assertions.assertTrue(test.isPresent());
        Assertions.assertEquals(track.getId(), test.get().getId());
        Assertions.assertEquals(track.getName(), test.get().getName());
        Assertions.assertEquals(track.getVotes(), test.get().getVotes());
        Assertions.assertEquals(track.getArtistIds(), test.get().getArtistIds());
        Assertions.assertEquals(track.getAlbumId(), test.get().getAlbumId());
    }

    private TrackEntity trackWithId(String id) {
        return new TrackEntity(
                id, "track-name", Set.of("artist-1", "artist-2"),
                "album-id", 2000L
        );
    }

}
