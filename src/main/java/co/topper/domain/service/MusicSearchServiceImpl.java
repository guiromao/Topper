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
    private final ArtistConverter artistConverter;
    private final AlbumConverter albumConverter;

    @Autowired
    public MusicSearchServiceImpl(SpotifyApi spotifyApi,
                                  TrackConverter trackConverter,
                                  ArtistConverter artistConverter,
                                  AlbumConverter albumConverter) {
        this.spotifyApi = spotifyApi;
        this.trackConverter = trackConverter;
        this.artistConverter = artistConverter;
        this.albumConverter = albumConverter;
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

    @Override
    public TrackDto findTrackById(String trackId) {
        GetTrackRequest trackRequest = spotifyApi.getTrack(trackId).build();

        try {
            Track track = trackRequest.execute();

            return trackConverter.toDto(track);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new ConnectivityFailureException(TrackEntity.class);
        }
    }

    @Override
    @Cacheable(value = CACHE_ARTIST_SERVICE)
    public Set<ArtistDto> searchArtists(String value) {
        SearchArtistsRequest artistsRequest = spotifyApi.searchArtists(value)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<Artist> artists = artistsRequest.execute();

            return artistConverter.toDtoSet(artists);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new ConnectivityFailureException(ArtistEntity.class);
        }
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

    @Override
    @Cacheable(value = CACHE_ALBUMS_SERVICE)
    public Set<AlbumDto> searchAlbums(String value) {
        final SearchAlbumsRequest searchAlbums = spotifyApi.searchAlbums(value)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<AlbumSimplified> albums = searchAlbums.execute();

            return albumConverter.toDtoSet(albums);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new ConnectivityFailureException(AlbumEntity.class);
        }
    }

    @Override
    public AlbumDto findAlbumById(String albumId) {
        GetAlbumRequest albumRequest = spotifyApi.getAlbum(albumId).build();

        try {
            Album album = albumRequest.execute();

            return albumConverter.toDto(album);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new ConnectivityFailureException(AlbumEntity.class);
        }
    }

    private void assertValidText(String text, Class clazz) {
        if ("".equals(text.trim())) {
            throw new EmptySearchTextException(clazz);
        }
    }

}
