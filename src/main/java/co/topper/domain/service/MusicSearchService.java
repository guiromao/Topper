package co.topper.domain.service;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.Set;

/**
 * Service that will use a 3rd party tool to collect music data
 */
public interface MusicSearchService {

    Set<TrackDto> searchTracks(String value);

    TrackDto findTrackById(String trackId);

    Set<ArtistDto> searchArtists(String value);

    ArtistDto findArtistById(String artistId);

    Set<AlbumDto> searchAlbums(String value);

    AlbumDto findAlbumById(String albumId);

}
