package co.topper.domain.scheduler;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.service.DataManager;
import co.topper.domain.service.MusicSearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class FeaturedTrackSchedulerTests {

    private static final String TRACK_ID = "track-id";
    private static final String TRACK_NAME = "track-name";
    private static final String ALBUM_ID = "album-id";
    private static final String ALBUM_NAME = "album-name";
    private static  final String RELEASE_DATE = "2022-07-24";
    private static final String ARTIST_ID = "artist-id";
    private static final String ARTIST_NAME = "artist-name";

    @Mock
    MusicSearchService musicSearchService;

    @Mock
    TrackRepository trackRepository;

    @Mock
    DataManager dataManager;

    @Autowired
    Random random;

    FeaturedTrackScheduler featuredTrackScheduler;

    @BeforeEach
    void setup() {
        featuredTrackScheduler = new FeaturedTrackScheduler(musicSearchService, trackRepository,
                dataManager, random);
    }

    @Test
    void testSaveFeaturedTrack() {
        when(musicSearchService.searchTracks(anyString())).thenReturn(Set.of(trackDto()));

        featuredTrackScheduler.saveFeaturedTrack();

        verify(dataManager, times(1)).handleDataOf(any(TrackDto.class));
        verify(trackRepository, times(1)).save(any(TrackEntity.class));
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

}
