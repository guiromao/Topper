package co.topper.domain.scheduler;

import co.topper.configuration.TrendyTermsConfiguration;
import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.service.DataManager;
import co.topper.domain.service.MusicSearchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class TrackOfTheHourScheduler {

    private static final int HOURS_IN_MILLISECONDS = 1000 * 60 * 60;
    private static final int NUMBER_OF_HOURS = 1;

    private final MusicSearchService searchService;
    private final TrackRepository trackRepository;
    private final DataManager dataManager;
    private final TrackConverter trackConverter;

    public TrackOfTheHourScheduler(MusicSearchService searchService,
                                   TrackRepository trackRepository,
                                   DataManager dataManager,
                                   TrackConverter trackConverter) {
        this.searchService = searchService;
        this.trackRepository = trackRepository;
        this.dataManager = dataManager;
        this.trackConverter = trackConverter;
    }

    @Scheduled(fixedRate = HOURS_IN_MILLISECONDS * NUMBER_OF_HOURS)
    public void selectRandomTopTrack() {
        final String searchTerm = pickTerm();
        TrackDto trackDto = getTrack(searchTerm);

        dataManager.handleDataOf(trackDto);

        TrackEntity track = TrackEntity.createTrackOfTheHour(
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
        int randomIndex = generateRandom(trackOptions.size());

        return trackOptions.get(randomIndex);
    }

    private String pickTerm() {
        List<String> trendyTerms = TrendyTermsConfiguration.getTrendyTerms();
        int randomIndex = generateRandom(trendyTerms.size());

        return trendyTerms.get(randomIndex);
    }

    private int generateRandom(int limit) {
        Random random = new Random();

        return random.nextInt(limit);
    }

}
