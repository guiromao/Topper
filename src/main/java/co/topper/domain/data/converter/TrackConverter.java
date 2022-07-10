package co.topper.domain.data.converter;

import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TrackConverter {

    private final ArtistConverter artistConverter;
    private final AlbumConverter albumConverter;

    @Autowired
    public TrackConverter(ArtistConverter artistConverter,
                          AlbumConverter albumConverter) {
        this.artistConverter = artistConverter;
        this.albumConverter = albumConverter;
    }

    public Set<TrackDto> toDtoSet(Paging<Track> tracks) {
        return Stream.of(tracks.getItems())
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public TrackDto toDto(Track track) {
        return new TrackDto(
                track.getId(),
                track.getName(),
                Stream.of(track.getArtists())
                        .map(artistConverter::toDto)
                        .collect(Collectors.toSet()),
                albumConverter.toDto(track.getAlbum())
        );
    }

    public List<TrackDto> toDtoList(List<TrackEntity> tracks,
                                    Set<AlbumEntity> albums,
                                    Set<ArtistEntity> artists) {

        return tracks.stream()
                .map(track -> new TrackDto(
                        track.getId(),
                        track.getName(),
                        Objects.nonNull(track.getArtistIds()) ?
                                artistConverter.toDtoSet(track, artists)
                                : null,
                        Objects.nonNull(track.getAlbumId()) ?
                                albumConverter.toDto(track, albums)
                                : null
                ))
                .toList();

    }

}
