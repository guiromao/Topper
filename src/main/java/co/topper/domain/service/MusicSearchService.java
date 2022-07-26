package co.topper.domain.service;

import co.topper.domain.data.dto.TrackDto;

import java.util.Set;

/**
 * Service that will use a 3rd party tool to collect music data
 */
public interface MusicSearchService {

    Set<TrackDto> searchTracks(String value);

}
