package co.topper.domain.data.dto;

import java.io.Serializable;
import java.util.Objects;

public class ArtistDto implements Serializable {

    private static final long serialVersionUID = 47567657L;

    private final String artistId;
    private final String name;

    public ArtistDto(String artistId, String name) {
        this.artistId = artistId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ArtistDto{" +
                "artistId='" + artistId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getArtistId() {
        return artistId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ArtistDto artistDto = (ArtistDto) o;
        return Objects.equals(artistId, artistDto.artistId) && Objects.equals(name, artistDto.name);
    }

    @Override public int hashCode() {
        return Objects.hash(artistId, name);
    }
}
