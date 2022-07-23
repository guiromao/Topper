package co.topper.domain.data.converter;

import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
class ArtistConverterTests {

    private static final String ARTIST_ID = "artist-id";
    private static final String ARTIST_NAME = "artist-name";

    @Autowired
    ArtistConverter artistConverter;

    @Test
    void testConvertFromArtist() {
        ArtistDto test = artistConverter.toDto(artist());

        Assertions.assertEquals(ARTIST_ID, test.getArtistId());
        Assertions.assertEquals(ARTIST_NAME, test.getName());
    }

    @Test
    void testConvertFromArtistPaging() {
        Artist[] artists = { artist() };
        Paging<Artist> artistPaging = new Paging.Builder<Artist>()
                .setItems(artists)
                .build();

        Set<ArtistDto> tests = artistConverter.toDtoSet(artistPaging);

        Assertions.assertTrue(
                tests.stream()
                        .allMatch(dto -> Stream.of(artists)
                                .anyMatch(artist -> artist.getId().equals(dto.getArtistId()) &&
                                        artist.getName().equals(dto.getName())))
        );
    }

    @Test
    void testConvertFromArtistSimplified() {
        ArtistDto test = artistConverter.toDto(artistSimplified());

        Assertions.assertEquals(ARTIST_ID, test.getArtistId());
        Assertions.assertEquals(ARTIST_NAME, test.getName());
    }

    @Test
    void testConvertFromEntity() {
        ArtistDto test = artistConverter.toDto(artistEntity());

        Assertions.assertEquals(ARTIST_ID, test.getArtistId());
        Assertions.assertEquals(ARTIST_NAME, test.getName());
    }

    @Test
    void testConvertFromTrackAndList() {
        Set<ArtistDto> tests = artistConverter.toDtoSet(track(), artists());

        Assertions.assertEquals(artists().size(), tests.size());
        Assertions.assertTrue(
                tests.stream()
                        .allMatch(dto -> (dto.getArtistId().equals(ARTIST_ID) ||
                                dto.getArtistId().equals("artist-2")) &&
                                (dto.getName().equals(ARTIST_NAME) ||
                                        dto.getName().equals("artist-name-2"))
                        )
        );
    }

    private TrackEntity track() {
        return new TrackEntity(
            "track-1",
            "track-name",
            Set.of(ARTIST_ID, "artist-2"),
            "album-1",
            10000L
        );
    }

    private List<ArtistEntity> artists() {
        return List.of(
                artistEntity(),
                new ArtistEntity("artist-2", "artist-name-2", "https://another-site.com/image-artist.png")
        );
    }

    private ArtistSimplified artistSimplified() {
        return new ArtistSimplified.Builder()
                .setId(ARTIST_ID)
                .setName(ARTIST_NAME)
                .build();
    }

    private Artist artist() {
        return new Artist.Builder()
                .setId(ARTIST_ID)
                .setName(ARTIST_NAME)
                .build();
    }

    private ArtistEntity artistEntity() {
        return new ArtistEntity(
            ARTIST_ID,
            ARTIST_NAME,
            "https://images-artists.com/image-artist.png"
        );
    }

}
