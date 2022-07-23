package co.topper.domain.data.converter;

import co.topper.domain.data.dto.ArtistDto;
import co.topper.domain.data.entity.ArtistEntity;
import co.topper.domain.data.entity.TrackEntity;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ArtistConverter {

    public Set<ArtistDto> toDtoSet(Paging<Artist> artists) {
        return Stream.of(artists.getItems())
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public ArtistDto toDto(Artist artist) {
        return new ArtistDto(
                artist.getId(),
                artist.getName()
        );
    }

    public ArtistDto toDto(ArtistSimplified artist) {
        return new ArtistDto(
                artist.getId(),
                artist.getName()
        );
    }

    public Set<ArtistDto> toDtoSet(TrackEntity track, List<ArtistEntity> artists) {
        Set<ArtistEntity> trackArtists = filterArtists(track, artists);

        return trackArtists.stream()
                .map(artist -> new ArtistDto(
                        artist.getId(),
                        artist.getName()
                ))
                .collect(Collectors.toSet());
    }

    public ArtistDto toDto(ArtistEntity artist) {
        return new ArtistDto(
                artist.getId(),
                artist.getName()
        );
    }

    private Set<ArtistEntity> filterArtists(TrackEntity track, List<ArtistEntity> artists) {
        if (Objects.isNull(track.getArtistIds())) {
            return Collections.emptySet();
        }

        return artists.stream()
                .filter(artist -> track.getArtistIds().contains(artist.getId()))
                .collect(Collectors.toSet());
    }

}
