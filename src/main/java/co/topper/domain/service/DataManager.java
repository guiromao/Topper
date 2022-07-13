package co.topper.domain.service;

import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;

import java.util.List;

/**
 * Responsible for creating Music entities, if they don't already exist in Database
 */
public interface DataManager {

    void handleDataOf(VoteDto voteDto);

    List<TrackEntity> getTracks(List<String> trackIds);

    List<AlbumEntity> getAlbums(List<String> albumIds);

    List<ArtistEntity> getArtists(List<String> artistIds);

}
