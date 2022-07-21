package co.topper.domain.data.converter;

import co.topper.domain.data.dto.AlbumDto;
import co.topper.domain.data.entity.AlbumEntity;
import co.topper.domain.data.entity.TrackEntity;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.Album;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AlbumConverter {

    public Set<AlbumDto> toDtoSet(Paging<AlbumSimplified> albums) {
        return Stream.of(albums.getItems())
                .map(this::toDto)
                .collect(Collectors.toSet());
    }

    public AlbumDto toDto(AlbumSimplified album) {
        return new AlbumDto(
                album.getId(),
                album.getName(),
                Stream.of(album.getArtists())
                        .map(ArtistSimplified::getId)
                        .collect(Collectors.toSet()),
                album.getReleaseDate()
        );
    }

    public AlbumDto toDto(Album album) {
        return new AlbumDto(
                album.getId(),
                album.getName(),
                Stream.of(album.getArtists())
                        .map(ArtistSimplified::getId)
                        .collect(Collectors.toSet()),
                album.getReleaseDate()
        );
    }

    public AlbumDto toDto(TrackEntity track, List<AlbumEntity> albums) {
        AlbumEntity albumEntity = extractAlbum(track, albums);

        if (Objects.isNull(albumEntity)) {
            return null;
        }

        return new AlbumDto(
                albumEntity.getId(),
                albumEntity.getName(),
                albumEntity.getArtistIds(),
                albumEntity.getReleaseDate()
        );
    }

    private AlbumEntity extractAlbum(TrackEntity track, List<AlbumEntity> albums) {
        return albums.stream()
                .filter(album -> album.getId().equals(track.getAlbumId()))
                .findFirst()
                .orElse(null);
    }

    public AlbumDto toDto(AlbumEntity album) {
        return new AlbumDto(
                album.getId(),
                album.getName(),
                album.getArtistIds(),
                album.getReleaseDate()
        );
    }

}
