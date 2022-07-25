package co.topper.domain.service;

import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class FeaturedTrackServiceTests {

    @Mock
    TrackRepository trackRepository;

    @Mock
    AlbumRepository albumRepository;

    @Mock
    ArtistRepository artistRepository;

    @Mock
    TrackConverter trackConverter;

    FeaturedTrackService featuredTrackService;

    @BeforeEach
    void setup() {
        featuredTrackService = new FeaturedTrackServiceImpl(trackRepository, albumRepository,
                artistRepository, trackConverter);
    }

    @Test
    void testNonExistingTrack() {
        when(trackRepository.getFeaturedTrack()).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> featuredTrackService.getFeaturedTrack()
        );
    }

    @Test
    void testNonExistingAlbum() {
        when(trackRepository.getFeaturedTrack()).thenReturn(Optional.of(track()));
        when(albumRepository.findById(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> featuredTrackService.getFeaturedTrack()
        );
    }

    @Test
    void testGetFeaturedTrack() {
        final TrackDto trackDto = trackDto();

        when(trackRepository.getFeaturedTrack()).thenReturn(Optional.of(track()));
        when(albumRepository.findById(anyString())).thenReturn(Optional.of(album()));
        when(artistRepository.findAllById(any(Set.class)))
                .thenReturn(List.of(artistWithId("artist-1"), artistWithId("artist-2")));
        when(trackConverter.toFeaturedDto(any(TrackEntity.class), any(AlbumEntity.class), any(List.class)))
                .thenReturn(trackDto);

        TrackDto test = featuredTrackService.getFeaturedTrack();

        Assertions.assertEquals(trackDto.getId(), test.getId());
        Assertions.assertEquals(trackDto.getName(), test.getName());
        Assertions.assertEquals(trackDto.getArtists(), test.getArtists());
        Assertions.assertEquals(trackDto.getAlbum(), test.getAlbum());
    }

    private TrackDto trackDto() {
        TrackEntity track = track();
        AlbumEntity album = album();

        return new TrackDto(
                track.getId(), track.getName(),
                Set.of(
                        new ArtistDto("artist-1", "artist-name-1"),
                        new ArtistDto("artist-2", "artist-name-2")
                ),
                new AlbumDto(album.getId(), album.getName(),
                        album.getArtistIds(), album.getReleaseDate())
        );
    }

    private TrackEntity track() {
        return new TrackEntity(
                "track-id", "track-name",
                Set.of("artist-1", "artist-2"),
                "album-id", 1000L
        );
    }

    private AlbumEntity album() {
        return new AlbumEntity(
                "album-id", "album-name",
                Set.of("artist-1", "artist-2"),
                "2022-07-25"
        );
    }

    private ArtistEntity artistWithId(String id) {
        return new ArtistEntity(id, "artist-name", "https//images.com/" + id + ".png");
    }

}
