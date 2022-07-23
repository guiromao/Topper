package co.topper.domain.data.converter;

import co.topper.domain.data.dto.FriendDto;
import co.topper.domain.data.dto.TopDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.Role;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FriendConverterTests {

    @Autowired
    FriendConverter friendConverter;

    @Test
    void testConvertFriend() {
        FriendDto test = friendConverter.toDto(userEntity(),
                                               trackVotes(),
                                               List.of(track()),
                                               List.of(album()),
                                               List.of(artist()));

        List<TopDto> friendTracks = test.getLikedTracks();

        assertEquals("user-1", test.getFriendId());
        assertEquals("username", test.getUsername());
        assertEquals("track-1", friendTracks.get(0).getId());
        assertEquals("artist-1", friendTracks.get(0).getArtists().iterator().next().getArtistId());
        assertEquals("album-1", friendTracks.get(0).getAlbum().getId());
        assertEquals(40000L, friendTracks.get(0).getTotalVotes());
    }

    private UserEntity userEntity() {
        return new UserEntity(
                "user-1",
                "username",
                "pass",
                "user@mail.com",
                Set.of("1", "2"),
                Set.of("3"),
                trackVotes(),
                20000L,
                Instant.now(),
                Set.of(Role.USER)
        );
    }

    private TrackEntity track() {
        return new TrackEntity(
                "track-1",
                "Track of my life",
                Set.of("artist-1"),
                "album-1",
                1000L
        );
    }

    private AlbumEntity album() {
        return new AlbumEntity(
                "album-1",
                "Album Name of my life",
                Set.of("artist-1"),
                "2022-07-23"
        );
    }

    private ArtistEntity artist() {
        return new ArtistEntity(
                "artist-1",
                "The Artist of my life",
                "artist-image.png"
        );
    }

    private Map<String, Long> trackVotes() {
        return Map.of("track-1", 40000L,
                "track-2", 5000L);
    }

}
