package co.topper.domain.data.converter;

import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.dto.TrackDto;
import co.topper.domain.data.entity.TrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;

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

}
