package co.topper.domain.service;

import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.TrackDto;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Integer NUMBER_RESULTS = 20;

    private final SpotifyApi spotifyApi;
    private final TrackConverter trackConverter;

    public SearchServiceImpl(SpotifyApi spotifyApi,
                             TrackConverter trackConverter) {
        this.spotifyApi = spotifyApi;
        this.trackConverter = trackConverter;
    }

    @Override
    public Set<TrackDto> searchTracks(String value) {
        SearchTracksRequest searchRequest = spotifyApi.searchTracks(value)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<Track> spotifyTracks = searchRequest.execute();

            return trackConverter.toDtoSet(spotifyTracks);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
        }

        return Collections.emptySet();
    }

}
