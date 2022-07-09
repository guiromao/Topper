package co.topper.domain.service;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;

import java.util.Set;

/**
 * Service that will use a 3rd party tool to collect music data
 */
public interface DiscoverService {

    Set<TrackDto> searchTracks(String value);

    TrackDto findTrackById(String trackId);

    Set<ArtistDto> searchArtists(String artist);

    ArtistDto findArtistById(String artistId);

    Set<AlbumDto> searchAlbums(String value);

    AlbumDto findAlbumById(String albumId);

}
