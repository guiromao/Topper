package co.topper.domain.service;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.dto.VoteDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataManagerImpl implements DataManager {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    @Autowired
    public DataManagerImpl(TrackRepository trackRepository,
                           AlbumRepository albumRepository,
                           ArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @Override
    public void handleDataOf(VoteDto voteDto) {
        handleTrack(voteDto);
        handleAlbum(voteDto.getAlbum());
        handleArtists(voteDto.getArtists());
    }

    private void handleTrack(TrackDto trackDto) {
        if (!trackRepository.existsById(trackDto.getId())) {
            TrackEntity track = TrackEntity.create(
                    trackDto.getId(),
                    trackDto.getName(),
                    Objects.nonNull(trackDto.getArtists()) ?
                            trackDto.getArtists().stream()
                                    .map(ArtistDto::getArtistId)
                                    .collect(Collectors.toSet())
                            : Collections.emptySet()
            );

            trackRepository.save(track);
        }
    }

    private void handleAlbum(AlbumDto albumDto) {
        if (Objects.nonNull(albumDto) && !albumRepository.existsById(albumDto.getId())) {
            AlbumEntity album = new AlbumEntity(
                    albumDto.getId(),
                    albumDto.getName(),
                    albumDto.getArtistIds(),
                    albumDto.getReleaseDate()
            );

            albumRepository.save(album);
        }
    }

    private void handleArtists(Set<ArtistDto> artistDtos) {
        if (Objects.nonNull(artistDtos)) {
            Set<ArtistEntity> artists = artistDtos.stream()
                    .map(this::handleArtist)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (!artists.isEmpty()) {
                artistRepository.saveAll(artists);
            }
        }
    }

    private ArtistEntity handleArtist(ArtistDto artistDto) {
        ArtistEntity artist = null;

        if (!artistRepository.existsById(artistDto.getArtistId())) {
            artist = new ArtistEntity(
                    artistDto.getArtistId(),
                    artistDto.getName(),
                    null
            );
        }

        return artist;
    }
}