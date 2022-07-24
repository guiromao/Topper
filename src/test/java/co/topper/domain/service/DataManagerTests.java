package co.topper.domain.service;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class DataManagerTests {

    static final String TRACK_ID_1 = "track-id-1";
    static final String ALBUM_ID_1 = "album-1";
    static final String ARTIST_ID_1 = "artist-id-1";

    @Mock
    TrackRepository trackRepository;

    @Mock
    AlbumRepository albumRepository;

    @Mock
    ArtistRepository artistRepository;

    DataManager dataManager;

    @BeforeEach
    void setup() {
        dataManager = new DataManagerImpl(trackRepository, albumRepository, artistRepository);
    }

    @Test
    void testHandleNonExistingEntities() {
        when(trackRepository.existsById(anyString())).thenReturn(false);
        when(albumRepository.existsById(anyString())).thenReturn(false);
        when(artistRepository.existsById(anyString())).thenReturn(false);

        dataManager.handleDataOf(trackDtoExample());

        verify(trackRepository, times(1)).save(any(TrackEntity.class));
        verify(albumRepository, times(1)).save(any(AlbumEntity.class));
        verify(artistRepository, times(1)).saveAll(any(Set.class));
    }

    @Test
    void testHandleExistingEntities() {
        when(trackRepository.existsById(anyString())).thenReturn(true);
        when(albumRepository.existsById(anyString())).thenReturn(true);
        when(artistRepository.existsById(anyString())).thenReturn(true);

        dataManager.handleDataOf(trackDtoExample());

        verify(trackRepository, times(0)).save(any(TrackEntity.class));
        verify(albumRepository, times(0)).save(any(AlbumEntity.class));
        verify(artistRepository, times(0)).saveAll(any(Set.class));
    }

    @Test
    void testGetTracks() {
        List<TrackEntity> tracks = List.of(trackWithId("track-1"), trackWithId("track-2"));

        when(trackRepository.findAllById(any(List.class))).thenReturn(tracks);

        List<TrackEntity> test = dataManager.getTracks(List.of("track-1", "track-2"));

        Assertions.assertEquals(tracks.size(), test.size());
    }

    @Test
    void testGetAlbums() {
        List<AlbumEntity> albums = List.of(albumWithId("album-1"), albumWithId("album-2"));

        when(albumRepository.findAllById(any(List.class))).thenReturn(albums);

        List<AlbumEntity> test = dataManager.getAlbums(List.of("album-1", "album-2"));

        Assertions.assertEquals(albums.size(), test.size());
    }

    @Test
    void testGetArtists() {
        List<ArtistEntity> artists = List.of(artistWithId("artist-1"), artistWithId("artist-2"));

        when(artistRepository.findAllById(any(List.class))).thenReturn(artists);

        List<ArtistEntity> test = dataManager.getArtists(List.of("artist-1", "artist-2"));

        Assertions.assertEquals(artists.size(), test.size());
    }

    private TrackDto trackDtoExample() {
        return new TrackDto(
            TRACK_ID_1,
            "track-name",
            Set.of(artistExample()),
            albumExample()
        );
    }

    private AlbumDto albumExample() {
        return new AlbumDto(ALBUM_ID_1, "album-name",
                Set.of(ARTIST_ID_1), "2022-07-24");
    }

    private ArtistDto artistExample() {
        return new ArtistDto(ARTIST_ID_1, "artist-name");
    }

    private TrackEntity trackWithId(String id) {
        return new TrackEntity(
                id, "name", Set.of("1", "2"), "album-id", 1000L
        );
    }

    private AlbumEntity albumWithId(String id) {
        return new AlbumEntity(
                id, "name", Set.of("1", "2"), "2022-07-24"
        );
    }

    private ArtistEntity artistWithId(String id) {
        return new ArtistEntity(
                id, "artist-name", "https://images.com/artist-image.png"
        );
    }

}
