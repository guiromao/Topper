package co.topper.domain.service;

import co.topper.domain.data.converter.ArtistConverter;
import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.exception.ConnectivityFailureException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static co.topper.configuration.RedisConfiguration.CACHE_TRACKS_SERVICE;

@Service
public class SearchServiceImpl implements SearchService {

    private static final Integer NUMBER_RESULTS = 20;

    private final SpotifyApi spotifyApi;
    private final TrackConverter trackConverter;
    private final ArtistConverter artistConverter;

    @Autowired
    public SearchServiceImpl(SpotifyApi spotifyApi,
                             TrackConverter trackConverter,
                             ArtistConverter artistConverter) {
        this.spotifyApi = spotifyApi;
        this.trackConverter = trackConverter;
        this.artistConverter = artistConverter;
    }

    @Override
    @Cacheable(value = CACHE_TRACKS_SERVICE)
    public Set<TrackDto> searchTracks(String value) {
        SearchTracksRequest searchRequest = spotifyApi.searchTracks(value)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<Track> spotifyTracks = searchRequest.execute();

            return trackConverter.toDtoSet(spotifyTracks);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            throw new ConnectivityFailureException(TrackEntity.class);
        }
    }

    @Override
    public Set<ArtistDto> searchArtists(String artist) {
        return null;
    }

    @Override
    public ArtistDto findArtistById(String artistId) {
        GetArtistRequest searchArtist = spotifyApi.getArtist(artistId).build();

        try {
            Artist artist = searchArtist.execute();
            return artistConverter.toDto(artist);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new ConnectivityFailureException(ArtistEntity.class);
        }
    }

}
