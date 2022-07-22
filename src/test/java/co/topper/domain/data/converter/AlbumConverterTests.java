package co.topper.domain.data.converter;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.TrackEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging.Builder;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
class AlbumConverterTests {

    private static final String ALBUM_ID = "album-id";
    private static final String ALBUM_NAME = "album-name";
    private static final String RELEASE_DATE = "\"2022-07-22\"";

    @Autowired
    AlbumConverter albumConverter;

    @Test
    void testConvertFromSimplifiedToDto() {
        AlbumDto test = albumConverter.toDto(albumSimplifiedExample());

        Assertions.assertEquals(ALBUM_ID, test.getId());
        Assertions.assertEquals(ALBUM_NAME, test.getName());
        Assertions.assertEquals(Set.of(artistExample().getId()), test.getArtistIds());
        Assertions.assertEquals(RELEASE_DATE, test.getReleaseDate());
    }

    @Test
    void testConvertFromSetToDto() {
        AlbumSimplified[] albums = { albumSimplifiedExample() };
        Set<AlbumDto> tests = albumConverter.toDtoSet(new Builder<AlbumSimplified>()
                .setItems(albums).build());

        Assertions.assertTrue(
                tests.stream()
                        .allMatch(albumDto -> Stream.of(albums)
                                        .anyMatch(simplified -> albumsEqual(albumDto, simplified)))
        );
    }

    @Test
    void testConvertFromAlbumToDto() {
        Album album = new Album.Builder()
                .setId(ALBUM_ID)
                .setName(ALBUM_NAME)
                .setReleaseDate(RELEASE_DATE)
                .setArtists(artistExample())
                .build();

        AlbumDto test = albumConverter.toDto(album);

        Assertions.assertEquals(ALBUM_ID, test.getId());
        Assertions.assertEquals(ALBUM_NAME, test.getName());
        Assertions.assertEquals(RELEASE_DATE, test.getReleaseDate());
        Assertions.assertTrue(Stream.of(artistExample())
                .allMatch(artistSimplified -> test.getArtistIds().stream()
                        .anyMatch(id -> id.equals(artistSimplified.getId())))
        );
    }

    @Test
    void testConvertWithTrackAndList() {
        List<AlbumEntity> albums = List.of(
                new AlbumEntity("album-1", "album-name-1", Set.of("1", "2"), "2022-07-22"),
                new AlbumEntity("album-2", "album-name-2", Set.of("3", "4"), "2022-08-23")
        );

        TrackEntity track = new TrackEntity("track-id", "track-name", Set.of("1", "2"),
                "album-1", 1000L);

        AlbumDto test = albumConverter.toDto(track, albums);

        Assertions.assertEquals("album-1", test.getId());
        Assertions.assertEquals("album-name-1", test.getName());
        Assertions.assertEquals(Set.of("1", "2"), test.getArtistIds());
        Assertions.assertEquals("2022-07-22", test.getReleaseDate());
    }

    @Test
    void testConvertFromEntity() {
        AlbumEntity entity = new AlbumEntity(ALBUM_ID, ALBUM_NAME, Set.of("5", "6"), "2022-01-01");

        AlbumDto test = albumConverter.toDto(entity);

        Assertions.assertEquals(ALBUM_ID, test.getId());
        Assertions.assertEquals(ALBUM_NAME, test.getName());
        Assertions.assertEquals("2022-01-01", test.getReleaseDate());
        Assertions.assertEquals(Set.of("5", "6"), test.getArtistIds());
    }

    private boolean albumsEqual(AlbumDto dto, AlbumSimplified simplified) {
        return simplified.getId().equals(dto.getId()) &&
                simplified.getName().equals(dto.getName()) &&
                simplified.getReleaseDate().equals(dto.getReleaseDate()) &&
                Stream.of(simplified.getArtists())
                        .allMatch(album -> dto.getArtistIds().stream()
                                .anyMatch(id -> album.getId().equals(id)));
    }

    private AlbumSimplified albumSimplifiedExample() {
        return new AlbumSimplified.Builder()
                .setId(ALBUM_ID)
                .setName(ALBUM_NAME)
                .setArtists(artistExample())
                .setReleaseDate(RELEASE_DATE)
                .build();
    }

    private ArtistSimplified artistExample() {
        return new ArtistSimplified.Builder()
                .setId("artist-id")
                .setName("artist-name")
                .build();
    }

}
