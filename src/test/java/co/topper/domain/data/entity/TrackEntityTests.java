package co.topper.domain.data.entity;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TrackEntityTests {

    @Test
    void testEquals() {
        EqualsVerifier.simple().forClass(TrackEntity.class)
                .withNonnullFields("id", "name", "votes")
                .verify();
    }

    @Test
    void testCreateTrack() {
        TrackEntity test = TrackEntity.create("track-id", "track-name",
                Set.of("artist-id-1", "artist-id-2"), "album-id");

        Assertions.assertEquals(0L, test.getVotes());
    }

    @Test
    void testCreateFeaturedTrack() {
        TrackEntity test = TrackEntity.createFeaturedTrack("track-name",
                Set.of("artist-id"), "album-id");

        Assertions.assertEquals(TrackEntity.FEATURED_TRACK_ID, test.getId());
    }

}
