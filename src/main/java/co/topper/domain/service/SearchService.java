package co.topper.domain.service;

import co.topper.domain.data.dto.TrackDto;

import java.util.Set;

public interface SearchService {

    Set<TrackDto> searchTracks(String value);

}
