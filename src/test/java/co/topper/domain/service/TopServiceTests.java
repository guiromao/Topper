package co.topper.domain.service;

import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TopDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

@SpringBootTest
class TopServiceTests {

    @Mock
    TrackRepository trackRepository;

    @Mock
    AlbumRepository albumRepository;

    @Mock
    ArtistRepository artistRepository;

    @Mock TrackConverter trackConverter;

    TopService topService;

    List<TopDto> topTracks = List.of(
            new TopDto("track-1", "track 1",
                    Set.of(new ArtistDto("artist-1", "artist 1")),
                    new AlbumDto("album-1", "album 1", Set.of("artist-1"),
                    "2022-08-15"),
            5000L),
            new TopDto("track-2", "track 2",
                    Set.of(new ArtistDto("artist-2", "artist 2")),
                    new AlbumDto("album-2", "album 2", Set.of("artist-2"),
                            "2022-08-15"),
                    2500L),
            new TopDto("track-3", "track 3",
                    Set.of(new ArtistDto("artist-3", "artist 3")),
                    new AlbumDto("album-3", "album 3", Set.of("artist-3"),
                            "2022-08-15"),
                    1000L)
    );

    List<TrackEntity> tracks = List.of(new TrackEntity("track-1", "track 1",
            Set.of("artist-1"), "album-1", 5000L));

    List<AlbumEntity> albums = List.of(new AlbumEntity("album-1", "album 1",
            Set.of("artist-1"), "2022-08-15"));

    List<ArtistEntity> artists = List.of(new ArtistEntity("artist-1", "artist 1",
            "https://images.com/artist-1.png"));

    @BeforeEach
    void setup() {
        topService = new TopServiceImpl(trackRepository, albumRepository,
                artistRepository, trackConverter);
    }

    @Test
    void getTop() {
        when(trackRepository.getTop(any(Integer.class), any(Integer.class)))
                .thenReturn(tracks);
        when(albumRepository.findAllById(any(Iterable.class))).thenReturn(albums);
        when(artistRepository.findAllById(any(Iterable.class))).thenReturn(artists);
        when(trackConverter.toTopDtoList(any(List.class), any(List.class),
                any(List.class), nullable(Map.class))).thenReturn(topTracks);

        List<TopDto> test = topService.getTop(0, 10);

        Assertions.assertEquals(topTracks.size(), test.size());
    }

}
