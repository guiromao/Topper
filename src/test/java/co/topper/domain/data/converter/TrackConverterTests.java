package co.topper.domain.data.converter;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TopDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
class TrackConverterTests {

    private static final String TRACK_ID = "track-id";
    private static final String TRACK_NAME = "track-name";
    private static final String ALBUM_ID = "album-id";
    private static final String ALBUM_NAME = "album-name";
    private static  final String RELEASE_DATE = "2022-07-24";
    private static final String ARTIST_ID = "artist-id";
    private static final String ARTIST_NAME = "artist-name";

    @Autowired
    TrackConverter trackConverter;

    @Test
    void testConvertFromTrack() {
        TrackDto test = trackConverter.toDto(track());

        Assertions.assertEquals(TRACK_ID, test.getId());
        Assertions.assertEquals(TRACK_NAME, test.getName());
        Assertions.assertEquals(Set.of(ARTIST_ID), test.getArtists().stream()
                .map(ArtistDto::getArtistId).collect(Collectors.toSet()));
        Assertions.assertEquals(ALBUM_ID, test.getAlbum().getId());
    }

    @Test
    void testConvertFromPagingTrack() {
        Paging<Track> pagingTracks = new Paging.Builder<Track>().setItems(new Track[] {track()}).build();
        Set<TrackDto> tests = trackConverter.toDtoSet(pagingTracks);

        List<String> trackIds = Stream.of(pagingTracks.getItems())
                .map(Track::getId).toList();
        List<String> artistIds = Stream.of(pagingTracks.getItems())
                .map(track -> List.of(track.getArtists()))
                .flatMap(Collection::stream)
                .map(ArtistSimplified::getId)
                .toList();
        List<String> albumIds = Stream.of(pagingTracks.getItems())
                .map(Track::getAlbum)
                .map(AlbumSimplified::getId)
                .toList();

        Assertions.assertTrue(tests.stream()
                .map(TrackDto::getId)
                .allMatch(trackIds::contains));
        Assertions.assertTrue(tests.stream()
                .map(TrackDto::getArtists)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .map(ArtistDto::getArtistId)
                .allMatch(artistIds::contains));
        Assertions.assertTrue(tests.stream()
                .map(TrackDto::getAlbum)
                .filter(Objects::nonNull)
                .map(AlbumDto::getId)
                .allMatch(albumIds::contains));
    }

    @Test
    void testConvertToTopList() {
        List<TopDto> tests = trackConverter.toTopDtoList(
                List.of(trackEntityWith(TRACK_ID, 1000L), trackEntityWith(TRACK_ID + "-2", 3000L)),
                List.of(albumEntity()),
                List.of(artistEntity()),
                Map.of(TRACK_ID, 1000L, TRACK_ID + "-2", 3000L)
        );

        Assertions.assertEquals(2, tests.size());
        Assertions.assertTrue(tests.stream()
                .map(TopDto::getId)
                .anyMatch(id -> id.equals(TRACK_ID)));
        Assertions.assertTrue(tests.stream()
                .map(TopDto::getId)
                .anyMatch(id -> id.equals(TRACK_ID + "-2")));

        for (int i = 0; i < tests.size() - 1; i++) {
            // Results should be ordered by number of votes
            Assertions.assertTrue(tests.get(i).getTotalVotes() >= tests.get(i + 1).getTotalVotes());
        }
    }

    @Test
    void testConvertToEntity() {
        TrackEntity test = trackConverter.toEntity(trackDto());

        Assertions.assertEquals(TRACK_ID, test.getId());
        Assertions.assertEquals(TRACK_NAME, test.getName());
        Assertions.assertTrue(test.getArtistIds().containsAll(trackDto().getArtists().stream()
                .map(ArtistDto::getArtistId).toList()));
        Assertions.assertEquals(trackDto().getAlbum().getId(), test.getAlbumId());
    }

    @Test
    void testConvertToFeaturedTrack() {
        List<ArtistEntity> artistsList = List.of(artistEntity());
        TrackDto test = trackConverter.toFeaturedDto(trackEntityWith(TRACK_ID, null),
                albumEntity(), artistsList);

        Assertions.assertEquals(TRACK_ID, test.getId());
        Assertions.assertEquals(TRACK_NAME, test.getName());
        Assertions.assertTrue(test.getAlbum().getId().equals(ALBUM_ID) &&
                test.getAlbum().getName().equals(ALBUM_NAME) &&
                test.getAlbum().getArtistIds().equals(Set.of(ARTIST_ID)));
        Assertions.assertTrue(
                test.getArtists().stream()
                        .allMatch(dto -> artistsList.stream()
                                .anyMatch(entity -> dto.getArtistId().equals(entity.getId()) &&
                                        dto.getName().equals(entity.getName())))
        );
    }

    private TrackEntity trackEntityWith(String id, Long votes) {
        return new TrackEntity(
                id,
                TRACK_NAME,
                Set.of(ARTIST_ID),
                ALBUM_ID,
                votes
        );
    }

    private AlbumEntity albumEntity() {
        return new AlbumEntity(
                ALBUM_ID, ALBUM_NAME,
                Set.of(ARTIST_ID),
                RELEASE_DATE
        );
    }

    private ArtistEntity artistEntity() {
        return new ArtistEntity(
                ARTIST_ID, ARTIST_NAME, "https://artists.com/artist-entity.png"
        );
    }

    private TrackDto trackDto() {
        return new TrackDto(
                TRACK_ID,
                TRACK_NAME,
                Set.of(new ArtistDto(
                        ARTIST_ID,
                        ARTIST_NAME
                )),
                new AlbumDto(
                        ALBUM_ID,
                        ALBUM_NAME,
                        Set.of(ARTIST_ID),
                        RELEASE_DATE
                )
        );
    }

    private Track track() {
        return new Track.Builder()
                .setId(TRACK_ID)
                .setName(TRACK_NAME)
                .setAlbum(albumSimplified())
                .setArtists(artistSimplified())
                .build();
    }

    private AlbumSimplified albumSimplified() {
        return new AlbumSimplified.Builder()
                .setId(ALBUM_ID)
                .setName(ALBUM_NAME)
                .setArtists(artistSimplified())
                .setReleaseDate(RELEASE_DATE)
                .build();
    }

    private ArtistSimplified artistSimplified() {
        return new ArtistSimplified.Builder()
                .setId(ARTIST_ID)
                .setName(ARTIST_NAME)
                .build();
    }

}
