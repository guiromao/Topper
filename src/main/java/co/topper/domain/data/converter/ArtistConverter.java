package co.topper.domain.data.converter;

import co.topper.domain.data.dto.ArtistDto;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.Artist;

@Component
public class ArtistConverter {

    public ArtistDto toDto(Artist artist) {
        return new ArtistDto(
                artist.getId(),
                artist.getName()
        );
    }

}
