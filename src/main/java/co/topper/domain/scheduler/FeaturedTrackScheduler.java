package co.topper.domain.scheduler;

import co.topper.configuration.TrendyTermsConfiguration;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.service.DataManager;
import co.topper.domain.service.MusicSearchService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Profile("!test")
@Component
public class FeaturedTrackScheduler {

    private static final int HOUR_IN_MILLISECONDS = 1000 * 60 * 60;
    private static final int NUMBER_OF_HOURS = 1;

    private final MusicSearchService searchService;
    private final TrackRepository trackRepository;
    private final DataManager dataManager;
    private final Random random;

    public FeaturedTrackScheduler(MusicSearchService searchService,
                                  TrackRepository trackRepository,
                                  DataManager dataManager,
                                  Random random) {
        this.searchService = searchService;
        this.trackRepository = trackRepository;
        this.dataManager = dataManager;
        this.random = random;
    }

    @Scheduled(fixedRate = HOUR_IN_MILLISECONDS * NUMBER_OF_HOURS)
    public void saveFeaturedTrack() {
        final String searchTerm = pickTerm();
        TrackDto trackDto = getTrack(searchTerm);

        dataManager.handleDataOf(trackDto);

        TrackEntity track = TrackEntity
                .createFeaturedTrack(
                trackDto.getName(),
                Objects.nonNull(trackDto.getArtists()) ?
                        trackDto.getArtists().stream()
                                .map(ArtistDto::getArtistId)
                                .collect(Collectors.toSet())
                        : null,
                Objects.nonNull(trackDto.getAlbum()) ?
                        trackDto.getAlbum().getId()
                        : null
        );

        trackRepository.save(track);
    }

    private TrackDto getTrack(String term) {
        List<TrackDto> trackOptions = searchService.searchTracks(term).stream().toList();
        int randomIndex = random.nextInt(trackOptions.size());

        return trackOptions.get(randomIndex);
    }

    private String pickTerm() {
        List<String> trendyTerms = TrendyTermsConfiguration.getTrendyTerms();
        int randomIndex = random.nextInt(trendyTerms.size());

        return trendyTerms.get(randomIndex);
    }

}
