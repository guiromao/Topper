package co.topper.domain.data.converter;

import co.topper.domain.data.dto.ArtistDto;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;

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

}
