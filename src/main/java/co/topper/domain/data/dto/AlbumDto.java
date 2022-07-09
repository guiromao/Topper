package co.topper.domain.data.dto;

import java.util.Objects;
import java.util.Set;

public class AlbumDto {

    private final String id;
    private final String name;
    private final Set<String> artistIds;
    private final String releaseDate;

    public AlbumDto(String id,
                    String name,
                    Set<String> artistIds,
                    String releaseDate) {
        this.id = id;
        this.name = name;
        this.artistIds = artistIds;
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "AlbumDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", artistIds=" + artistIds +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<String> getArtistIds() {
        return artistIds;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AlbumDto albumDto = (AlbumDto) o;
        return id.equals(albumDto.id) && name.equals(albumDto.name) && Objects.equals(artistIds, albumDto.artistIds) &&
                Objects.equals(releaseDate, albumDto.releaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artistIds, releaseDate);
    }
}
