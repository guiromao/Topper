package co.topper.domain.service;

import co.topper.domain.data.converter.AlbumConverter;
import co.topper.domain.data.converter.ArtistConverter;
import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.exception.ConnectivityFailureException;
import co.topper.domain.exception.EmptySearchTextException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.albums.GetAlbumRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchTracksRequest;
import se.michaelthelin.spotify.requests.data.tracks.GetTrackRequest;

import java.io.IOException;
import java.util.Set;

import static co.topper.configuration.RedisConfiguration.CACHE_ALBUMS_SERVICE;
import static co.topper.configuration.RedisConfiguration.CACHE_ARTIST_SERVICE;
import static co.topper.configuration.RedisConfiguration.CACHE_TRACKS_SERVICE;

@Service
public class MusicSearchServiceImpl implements MusicSearchService {

    private static final Integer NUMBER_RESULTS = 5;

    private final SpotifyApi spotifyApi;
    private final TrackConverter trackConverter;

    @Autowired
    public MusicSearchServiceImpl(SpotifyApi spotifyApi,
                                  TrackConverter trackConverter) {
        this.spotifyApi = spotifyApi;
        this.trackConverter = trackConverter;
    }

    @Override
    @Cacheable(value = CACHE_TRACKS_SERVICE)
    public Set<TrackDto> searchTracks(String value) {
        assertValidText(value, TrackEntity.class);

        SearchTracksRequest searchRequest = spotifyApi.searchTracks(value)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<Track> spotifyTracks = searchRequest.execute();

            return trackConverter.toDtoSet(spotifyTracks);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new ConnectivityFailureException(TrackEntity.class);
        }
    }

    private void assertValidText(String text, Class clazz) {
        if ("".equals(text.trim())) {
            throw new EmptySearchTextException(clazz);
        }
    }

}
