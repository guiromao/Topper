package co.topper.domain.service;

import co.topper.domain.data.converter.TrackConverter;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import co.topper.domain.data.repository.AlbumRepository;
import co.topper.domain.data.repository.ArtistRepository;
import co.topper.domain.data.repository.TrackRepository;
import co.topper.domain.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FeaturedTrackServiceImpl implements FeaturedTrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final TrackConverter trackConverter;

    @Autowired
    public FeaturedTrackServiceImpl(TrackRepository trackRepository,
                                    AlbumRepository albumRepository,
                                    ArtistRepository artistRepository,
                                    TrackConverter trackConverter) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
        this.trackConverter = trackConverter;
    }

    @Override
    public TrackDto getFeaturedTrack() {
        TrackEntity track = trackRepository.getFeaturedTrack()
                .orElseThrow(() -> new ResourceNotFoundException(TrackEntity.FEATURED_TRACK_ID, TrackEntity.class));

        AlbumEntity album = Objects.nonNull(track.getAlbumId()) ?
                albumRepository.findById(track.getAlbumId())
                        .orElseThrow(() -> new ResourceNotFoundException(track.getAlbumId(), AlbumEntity.class))
                : null;

        List<ArtistEntity> artists = (List<ArtistEntity>) artistRepository.findAllById(track.getArtistIds());

        return trackConverter.toFeaturedDto(track, album, artists);
    }

}
