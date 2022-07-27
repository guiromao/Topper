package co.topper.domain.service;

import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.exception.EmptySearchTextException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MusicSearchServiceTests {

    @Autowired
    SpotifyApi spotifyApi;

    @Mock
    TrackConverter trackConverter;

    MusicSearchService musicSearchService;

    @BeforeEach
    void setup() {
        musicSearchService = new MusicSearchServiceImpl(spotifyApi, trackConverter);
    }

    @Test
    void testInvalidSearchText() {
        Assertions.assertThrows(
                EmptySearchTextException.class,
                () -> musicSearchService.searchTracks("")
        );
    }

    @Test
    void testSearchTracks() {
        when(trackConverter.toDtoSet(any(Paging.class))).thenReturn(Set.of(trackDto()));

        Set<TrackDto> test = musicSearchService.searchTracks("song");

        Assertions.assertNotNull(test);
    }

    private TrackDto trackDto() {
        return new TrackDto(
                "track-id", "track-name",
                Set.of(new ArtistDto("artist-id", "artist-name")),
                new AlbumDto("album-id", "album-name", Set.of("artist-id"), "2022-07-27")
        );
    }

}
