package co.topper.domain.service;

import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.TopDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopServiceImpl implements TopService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final TrackConverter trackConverter;

    @Autowired
    public TopServiceImpl(TrackRepository trackRepository,
                          AlbumRepository albumRepository,
                          ArtistRepository artistRepository,
                          TrackConverter trackConverter) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.trackConverter = trackConverter;
    }

    @Override
    public List<TopDto> getTop(Integer offset, Integer limit) {
        List<TrackEntity> tracks = trackRepository.getTop(offset, limit);
        List<AlbumEntity> albums = fetchAlbums(extractAlbumIds(tracks));
        List<ArtistEntity> artists = fetchArtists(extractArtistIds(tracks));

        return trackConverter.toTopDtoList(tracks, albums, artists, null);
    }

    private List<AlbumEntity> fetchAlbums(Set<String> albumIds) {
        return (List<AlbumEntity>) albumRepository.findAllById(albumIds);
    }

    private List<ArtistEntity> fetchArtists(Set<String> artistIds) {
        return (List<ArtistEntity>) artistRepository.findAllById(artistIds);
    }

    private Set<String> extractAlbumIds(List<TrackEntity> tracks) {
        return tracks.stream()
                .map(TrackEntity::getAlbumId)
                .collect(Collectors.toSet());
    }

    private Set<String> extractArtistIds(List<TrackEntity> tracks) {
        return tracks.stream()
                .map(TrackEntity::getArtistIds)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

}
